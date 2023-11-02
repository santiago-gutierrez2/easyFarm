import {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import users from "../../users";
import {getFoodPurchaseById, updateFoodPurchase} from "../../../backend/FoodPurchaseService";
import {error} from "../../app/actions";
import {BounceLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import errors from "../../common/components/Errors";


const UpdateFoodPurchase = () => {

    const {foodPurchaseId} = useParams();
    const user = useSelector(users.selectors.getUser);
    const history = useHistory();
    const [foodPurchase, setFoodPurchase] = useState(null);
    const [productName, setProductName] = useState('');
    const [ingredients, setIngredients] = useState('');
    const [supplier, setSupplier] = useState('');
    const [kilos, setKilos] = useState(1);
    const [unitPrice, setUnitPrice] = useState(1.00);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;

    useEffect(async () => {
        const id = Number(foodPurchaseId);
        if (isLoading) {
            getFoodPurchaseById(id, (foodPurchaseDto) => {
                setFoodPurchase(foodPurchaseDto);
                // set state of the form
                setProductName(foodPurchaseDto.productName);
                setIngredients(foodPurchaseDto.ingredients ? foodPurchaseDto.ingredients : '');
                setSupplier(foodPurchaseDto.supplier ? foodPurchaseDto.supplier : '');
                setKilos(foodPurchaseDto.kilos);
                setUnitPrice(foodPurchaseDto.unitPrice);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
        }
    }, [isLoading, foodPurchase]);

    function handleUnitPriceChange(value) {
        setUnitPrice(Number(parseFloat(value).toFixed(2)))
    }

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            let foodPurchaseDTO = {
                productName: productName.trim(),
                ingredients: ingredients != null ? ingredients.trim() : null,
                supplier: supplier != null ? supplier.trim() : null,
                kilos: kilos,
                unitPrice: unitPrice,
            }
            updateFoodPurchase(Number(foodPurchaseId), foodPurchaseDTO,
                () => {
                    setSuccess('Food purchase updated correctly.');
                }, errors => setBackendErrors(errors)
            );
        } else {
            setBackendErrors(null);
            form.classList.add('war-validated');
        }
    }

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <BounceLoader color="#343A40" />
                </div>
            </div>
        );
    }

    return (
        <div className="row justify-content-center">
            <div className="col-7">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}></Success>
                <div className="card bg-light border-dark">
                    <h5 className="card-header card-title-custom ">
                        <FormattedMessage id="project.foodPurchase.update"/>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                            className="needs-validation"
                            onSubmit={e => handleSubmit(e)}>
                            <div className="form-group row">
                                <label htmlFor="productName" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodPurchase.productName"/>
                                </label>
                                <div className="col-md-4">
                                    <input type="text" id="productName" className="form-control"
                                       value={productName}
                                       onChange={e => setProductName(e.target.value)}
                                       autoFocus
                                       required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id="project.global.validator.required"/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="ingredients" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodPurchase.ingredients"/>
                                </label>
                                <div className="col-md-4">
                                    <textarea className="form-control" id="ingredients" rows="3"
                                        value={ingredients}
                                        onChange={e => setIngredients(e.target.value)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="supplier" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodPurchase.supplier"/>
                                </label>
                                <div className="col-md-4">
                                    <input type="text" id="supplier" className="form-control"
                                       value={supplier}
                                       onChange={e => setSupplier(e.target.value)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="kilos" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodPurchase.kilos"/>
                                </label>
                                <div className="col-md-4">
                                    <input type="number" min="1" step="1" className="form-control"
                                       value={kilos}
                                       pattern="[0-9]{10}"
                                       required
                                       onChange={e => setKilos(Number(e.target.value))}/>
                                </div>
                                <div className="invalid-feedback">
                                    <FormattedMessage id="project.global.validator.integer"/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="unitPrice" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodPurchase.unitPrice"/>
                                </label>
                                <div className="col-md-4">
                                    <input type="number" min="1" step="0.25" className="form-control"
                                           value={unitPrice}
                                           required
                                           onChange={e => handleUnitPriceChange(e.target.value)}/>
                                </div>
                                <div className="invalid-feedback">
                                    <FormattedMessage id="project.global.validator.required"/>
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

export default UpdateFoodPurchase;