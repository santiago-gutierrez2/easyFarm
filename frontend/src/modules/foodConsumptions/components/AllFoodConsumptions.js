import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import * as actions from '../actions';
import * as selectors from '../selectors';
import {PuffLoader} from "react-spinners";
import {FormattedDate, FormattedMessage} from "react-intl";
import {getAllAnimalsWithLabel, getAnimalById} from "../../../backend/animalService";
import {getListOfAllFoodPurchases} from "../../../backend/FoodPurchaseService";
import { MultiSelect } from "react-multi-select-component";
import DatePicker from "react-datepicker";
import {Link} from "react-router-dom";
import {Pager} from "../../common";
import * as commonActions from '../../app/actions';
import Modal from 'react-bootstrap/Modal';
import {Button} from "react-bootstrap";
import {deleteFoodConsumption} from "../../../backend/foodConsumptionService";
import Breadcrumb from "react-bootstrap/Breadcrumb";

const AllFoodConsumptions = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [animal, setAnimal] = useState('0');
    const [foodBatch, setFoodBatch] = useState('0');
    const [foodBatches, setFoodBatches] = useState([]);
    const [animalsOptions, setAnimalsOptions] = useState([]);
    const [animalsSelected, setAnimalsSelected] = useState([]);
    const foodConsumptionsSearch = useSelector(selectors.getFoodConsumptionsSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    // set active header item.
    dispatch(commonActions.activeItem('Item5'));

    useEffect(() => {
        if (isLoading) {
            // get animals
            getAllAnimalsWithLabel(true, false,(animals) => {
                setAnimalsOptions(animals);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
            // get foodBatches
            getListOfAllFoodPurchases( (foodBatchesDto) => {
                    setFoodBatches(foodBatchesDto);
                    setFoodBatch('0');
                }, error => setBackendErrors(error)
            );
            dispatch(actions.getALLFoodConsumptions({page: 0},
                (result) => {
                    console.log(result);
                    setIsLoading(false);
                    setBackendErrors(null);
                }, (errors) => {
                    setBackendErrors(errors);
                    setIsLoading(false);
                })
            );
        }
    }, [isLoading, dispatch]);

    function handleFilter() {
        let animalsIdsSelected = [];
        animalsIdsSelected = animalsSelected.map(a => a.value);
        let criteria = {
            page: 0,
            animals: animalsIdsSelected,
            foodBatch: foodBatch,
            startDate: startDate != null ? startDate : null,
            endDate: endDate != null ? endDate : null
        }
        dispatch(actions.getALLFoodConsumptions(criteria,
            (result) => {
                console.log(result);
            }, errors => {
                setBackendErrors(errors);
            })
        );
    }

    function handleDelete(foodConsumptionId) {
        deleteFoodConsumption(foodConsumptionId, () => {
            console.log('Borrado con exito');
            setShow(false);
            setIsLoading(true);
        }, errors => setBackendErrors(errors))
    }

    function getAnimalNameAndCode(animalId) {
        for (let animal of animalsOptions) {
            if (animal.value === animalId) {
                return animal.label;
            }
        }
    }

    function getFoodBatchProductName(foodPurchaseId) {
        for (let fb of foodBatches) {
            if (fb.id === foodPurchaseId) {
                return fb.productName;
            }
        }
    }

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <PuffLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    if (!foodConsumptionsSearch) {
        return null;
    }

    return (
        <>
            <Breadcrumb>
                <Breadcrumb.Item href="/">Dashboard</Breadcrumb.Item>
                <Breadcrumb.Item active>Food consumptions list</Breadcrumb.Item>
            </Breadcrumb>
            <div className="container">
                <div className="row mb-3">
                    <div className="col-sm-4">
                        <label className="col-form-label"><FormattedMessage
                            id="project.foodConsumption.animals"/></label>
                        <MultiSelect id="animals" options={animalsOptions} value={animalsSelected}
                                     onChange={setAnimalsSelected} labelledBy="Select"/>
                    </div>
                    <div className="col-sm-3">
                        <label className="col-form-label"><FormattedMessage
                            id="project.foodConsumption.foodBatch"/></label>
                        <select id="foodBatch" className="form-control" required
                                value={foodBatch}
                                onChange={e => setFoodBatch(e.target.value)}>
                            <option key={0} value="0">-- All options --</option>
                            {foodBatches && foodBatches.map(fb =>
                                <option key={fb.id} value={fb.id}>{fb.productName}</option>
                            )}
                        </select>
                    </div>
                    <div className="col-sm-3">
                        <label className="col-form-label"><FormattedMessage id="project.foodConsumption.date"/>:
                        </label>
                        <div className="w-100"></div>
                        <DatePicker className="form-control" selected={startDate}
                                    onChange={(date) => setStartDate(date)}/>
                        <DatePicker className="form-control mt-1" selected={endDate}
                                    onChange={(date) => setEndDate(date)}/>
                    </div>
                    <div className="col-sm-2 text-center">
                        <button className="btn btn-primary" style={{marginTop: '37px'}} onClick={e => handleFilter()}>
                            <FormattedMessage id="project.global.search"/></button>
                    </div>
                </div>
                {foodConsumptionsSearch.result.items.length === 0 &&
                    <div className="alert alert-danger" role="alert">
                        <FormattedMessage id="project.foodConsumption.foodConsumptionNotFound"/>
                    </div>
                }
                {foodConsumptionsSearch.result.items.map(foodConsumption => {
                    return (
                        <div key={foodConsumption.id} className="card mt-2 bg-light">
                            <div className="card-header container card-title-custom">
                                <h3><FormattedMessage id="project.foodConsumption.titleCard"/></h3>
                            </div>
                            <div className="card-body">
                                <p className="card-text">
                                    <b><FormattedMessage id="project.foodConsumption.animals"/>:</b> <Link className="link"
                                        to={`/animal/${foodConsumption.consumedBy}`}>{getAnimalNameAndCode(foodConsumption.consumedBy)}</Link>
                                </p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodConsumption.foodBatch"/>:</b> {getFoodBatchProductName(foodConsumption.foodBatch)}
                                </p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodConsumption.kilos"/>:</b> {foodConsumption.kilos}</p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodConsumption.date"/>:</b> <FormattedDate
                                    value={new Date(foodConsumption.date)}/></p>
                                <button className="btn btn-danger" onClick={() => handleShow()}>
                                    <FormattedMessage id="project.foodConsumption.delete"/> <i
                                    className="fas fa-trash"></i>
                                </button>
                                <Modal show={show} onHide={handleClose}>
                                    <Modal.Header>
                                        <Modal.Title>Deleting food consumption</Modal.Title>
                                    </Modal.Header>
                                    <Modal.Body>Are you sure to delete this consumption?</Modal.Body>
                                    <Modal.Footer>
                                        <Button variant="secondary" onClick={handleClose}>
                                            Cancel
                                        </Button>
                                        <Button variant="danger" onClick={() => handleDelete(foodConsumption.id)}>
                                            Delete
                                        </Button>
                                    </Modal.Footer>
                                </Modal>
                            </div>
                        </div>
                    );
                })}
                <div className="row mt-4">
                    <div className="col-12">
                        <Pager
                            back={{
                                enabled: foodConsumptionsSearch.criteria.page >= 1,
                                onClick: () => dispatch(actions.previousGetAllFoodConsumptions(foodConsumptionsSearch.criteria))
                            }}
                            next={{
                                enabled: foodConsumptionsSearch.result.existMoreItems,
                                onClick: () => dispatch(actions.nextGetAllFoodConsumptions(foodConsumptionsSearch.criteria))
                            }}
                        />
                    </div>
                </div>
            </div>
        </>

    );
}

export default AllFoodConsumptions;