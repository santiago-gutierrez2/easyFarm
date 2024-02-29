import {useState} from "react";
import {FormattedDate, FormattedMessage} from "react-intl";
import {Errors, Pager} from "../../common";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {useEffect} from "react";
import {BounceLoader, MoonLoader, PuffLoader} from "react-spinners";
import {useDispatch, useSelector} from "react-redux";
import * as actions from '../actions';
import * as selectors from '../selectors';
import {Link} from "react-router-dom";
import {getEmployees} from "../../../backend/userService";
import * as commonActions from "../../app/actions";
import Breadcrumb from "react-bootstrap/Breadcrumb";

const AllFoodPurchases = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [productName, setProductName] = useState('');
    const [supplier, setSupplier] = useState('');
    const [madeBy, setMadeBy] = useState('0');
    const foodPurchasesSearch = useSelector(selectors.getFoodPurchasesSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);

    // set active header item.
    dispatch(commonActions.activeItem('Item3'));

    useEffect( () => {
        if (isLoading) {
            dispatch(actions.getAllFoodPurchases({page: 0},
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
        let criteria = {
            page: 0,
            productName: productName.length > 0 ? productName : null,
            supplier: supplier.length > 0 ? supplier : null,
            startDate: startDate != null ? startDate : null,
            endDate: endDate != null ? endDate : null,
        }
        dispatch(actions.getAllFoodPurchases(criteria,
            (result) => {
                console.log(result);
            }, errors => {
                setBackendErrors(errors);
            })
        );
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

    if (!foodPurchasesSearch) {
        return null;
    }

    return (
        <>
            <Breadcrumb>
                <Breadcrumb.Item href="/">Dashboard</Breadcrumb.Item>
                <Breadcrumb.Item active>Food purchases list</Breadcrumb.Item>
            </Breadcrumb>
            <div className="container">
                <div className="row mb-3">
                    <div className="col-sm-4">
                        <label className="col-form-label"><FormattedMessage id="project.foodPurchase.productName"/>:
                        </label>
                        <input type="text" placeholder="Product name" className="form-control" value={productName}
                               onChange={e => setProductName(e.target.value)}/>
                    </div>
                    <div className="col-sm-3">
                        <label className="col-form-label"><FormattedMessage id="project.foodPurchase.supplier"/>:
                        </label>
                        <input type="text" placeholder="Supplier" className="form-control" value={supplier}
                               onChange={e => setSupplier(e.target.value)}/>
                    </div>
                    <div className="col-sm-3">
                        <label className="col-form-label"><FormattedMessage id="project.issues.creationDate"/>: </label>
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
                {foodPurchasesSearch.result.items.length === 0 &&
                    <div className="alert alert-danger" role="alert">
                        <FormattedMessage id='project.foodPurchase.foodPurchasesNotFound'/>
                    </div>
                }
                {foodPurchasesSearch.result.items.map(foodPurchase => {
                    return (
                        <div key={foodPurchase.id} className="card mt-2 bg-light">
                            <div className="card-header container card-title-custom">
                                <div className="row">
                                    <div className="col-xl-10 col-7">
                                        <h3>{foodPurchase.productName}</h3>
                                    </div>
                                    <div className="col-xl-2 col-5 align-self-center">
                                        <h5>
                                        <span className="badge badge-secondary float-right">
                                            Total: {foodPurchase.unitPrice * foodPurchase.kilos}€
                                        </span>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                            <div className="card-body">
                                <p className="card-text">{foodPurchase.ingredients}</p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodPurchase.supplier"/>:</b> {foodPurchase.supplier}</p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodPurchase.purchaseDate"/>:</b> <FormattedDate
                                    value={new Date(foodPurchase.purchaseDate)}/></p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodPurchase.kilos"/>:</b> {foodPurchase.kilos}</p>
                                <p className="card-text"><b><FormattedMessage
                                    id="project.foodPurchase.unitPrice"/>:</b> {foodPurchase.unitPrice}€</p>
                                {/*<div className="row">
                              <div className="col-6">*/}
                                <Link className="btn btn-primary" to={`/foodPurchase/${foodPurchase.id}`}>
                                    <FormattedMessage id="project.foodPurchase.viewDetails"/>
                                </Link>
                                {/*</div>
                          </div>*/}
                            </div>
                        </div>
                    )
                })}
                <div className="row mt-4">
                    <div className="col-12">
                        <Pager
                            back={{
                                enabled: foodPurchasesSearch.criteria.page >= 1,
                                onClick: () => dispatch(actions.previousGetAllFoodPurchases(foodPurchasesSearch.criteria))
                            }}
                            next={{
                                enabled: foodPurchasesSearch.result.existMoreItems,
                                onClick: () => dispatch(actions.nextGetAllFoodPurchases(foodPurchasesSearch.criteria))
                            }}
                        />
                    </div>
                </div>
            </div>
        </>
    );

}

export default AllFoodPurchases;