import {useHistory} from "react-router-dom";
import {useSelector} from "react-redux";
import users from "../../users";
import {useState} from "react";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import {createFoodPurchase} from "../../../backend/FoodPurchaseService";


const CreateFoodPurchase = () => {

    const history = useHistory();
    const user = useSelector(users.selectors.getUser);
    const [productName, setProductName] = useState('');
    const [ingredients, setIngredients] = useState('');
    const [supplier, setSupplier] = useState('');
    const [kilos, setKilos] = useState(1);
    const [unitPrice, setUnitPrice] = useState(1.00);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;

    function handleUnitPriceChange(value) {
        setUnitPrice(Number(parseFloat(value).toFixed(2)))
    }

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let foodPurchase = {
                productName: productName.trim(),
                ingredients: ingredients != null ? ingredients.trim() : null,
                supplier: supplier != null ? supplier.trim() : null,
                kilos: kilos,
                unitPrice: unitPrice,
                madeBy: Number.parseInt(user.id)
            }
            createFoodPurchase(foodPurchase,
                () => {
                    setSuccess('Food purchase created correctly');
                    setProductName('');
                    setIngredients('');
                    setSupplier('');
                    setKilos(1);
                    setUnitPrice(1.00);
                }, errors => setBackendErrors(errors)
            );
        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }
    }

    return (
        <div className="row justify-content-center">
            <div className="col-7">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}/>
                <div className="card bg-light border-dark">
                    <h5 className="card-header">
                        <FormattedMessage id="project.foodPurchase.create"></FormattedMessage>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                            className="needs-validation" noValidate
                            onSubmit={event => handleSubmit(event)}>
                            <div className="form-group row">
                                <label htmlFor="productName" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.foodPurchase.productName" />
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

export default CreateFoodPurchase;