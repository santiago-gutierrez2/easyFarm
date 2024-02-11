import {useEffect, useState} from "react";
import { MultiSelect } from "react-multi-select-component";
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {getAllAvailableFoodBatches} from "../../../backend/FoodPurchaseService";
import {MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import {createSeveralFoodConsumptions} from "../../../backend/foodConsumptionService";
import * as commonActions from "../../app/actions";
import {useDispatch} from "react-redux";

const CreateFoodConsumption = () => {

    const [kilos, setKilos] = useState(1);
    const [animalsOptions, setAnimalsOptions] = useState([]);
    const [animalsSelected, setAnimalsSelected] = useState([]);
    const [foodBatches, setFoodBatches] = useState([]);
    const [foodBatchSelected, setFoodBatchSelected] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    const dispatch = useDispatch();
    let form;

    // set active header item.
    dispatch(commonActions.activeItem('Item5'));

    useEffect(() => {
        if (isLoading) {
            // get available food batches
            getAllAvailableFoodBatches((availableFoodBatches) => {
                setFoodBatches(availableFoodBatches);
                setFoodBatchSelected(availableFoodBatches[0].id);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
            });
            // get animals
            getAllAnimalsWithLabel(false, false,(animals) => {
                setAnimalsOptions(animals);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
        }
    }, [isLoading]);

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity() && animalsSelected.length > 0 && kilos <= getAvailableKilos()) {
            let foodConsumptions = [];
            for (let animal of animalsSelected) {
                let foodConsumption = {
                    kilos: kilos,
                    foodBatch: Number.parseInt(foodBatchSelected),
                    consumedBy: animal.value
                }
                foodConsumptions.push(foodConsumption);
            }
            console.log(foodConsumptions);
            createSeveralFoodConsumptions(foodConsumptions,
                () => {
                    setSuccess('Food consumptions created correctly');
                    setIsLoading(true);
                }, errors => setBackendErrors(errors)
            );
        } else {
            if (animalsSelected.length === 0) {
                let warningElement = document.getElementById('animals-required');
                warningElement.style.display= 'block';
            }
            setBackendErrors(null);
            form.classList.add('was-validated');
        }
    }

    function getAvailableKilos() {
        for (let fb of foodBatches) {
            if (fb.id === Number.parseInt(foodBatchSelected)) {
                return fb.availableKilos;
            }
        }
    }

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <MoonLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
        <div className="row justify-content-center fade-in">
            <div className="col-sm-7 col-12">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}></Success>
                <div className="card bg-light">
                    <h5 className="card-header card-title-custom">
                        <FormattedMessage id="project.foodConsumption.create" />
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                            className="needs-validation" noValidate
                            onSubmit={event => handleSubmit(event)}>
                            <div className="form-group row">
                                <label htmlFor="foodBatch" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodConsumption.foodBatch"/>
                                </label>
                                <div className="col-md-6">
                                    <select id="foodBatch" className="form-control" required
                                        value={foodBatchSelected}
                                        onChange={e => setFoodBatchSelected(e.target.value)}>
                                        {foodBatches && foodBatches.map(fb =>
                                        <option key={fb.id} value={fb.id}>{fb.productName}</option>
                                        )}
                                    </select>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="animals" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodConsumption.animals"/>
                                </label>
                                <div className="col-md-6">
                                    <MultiSelect id="animals" required options={animalsOptions} value={animalsSelected} onChange={setAnimalsSelected} labelledBy="Select" />
                                    <div id="animals-required" className="invalid-feedback">
                                        <FormattedMessage id="project.global.validator.required"/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="kilos" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodConsumption.kilos"/>
                                </label>
                                <div className="col-md-6">
                                    <input type="number" min="1" max={getAvailableKilos()}
                                        value={kilos}
                                        required
                                        id="kilos"
                                        className="form-control"
                                        onChange={e => setKilos(Number(e.target.value))}/>
                                    <span className="badge badge-secondary float-right">
                                            Max Kilos: {getAvailableKilos()}
                                    </span>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id="project.global.validator.max"/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <div className="offset-md-3 col-md-2">
                                    <button type="submit" className="btn btn-primary">
                                        <FormattedMessage id="project.global.buttons.create"/>
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CreateFoodConsumption;