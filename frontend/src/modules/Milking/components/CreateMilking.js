import {useEffect, useState} from "react";
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import {registerMilking} from "../../../backend/milkingService";

const CreateMilking = () => {

    const [liters, setLiters] = useState(1);
    const [animals, setAnimals] = useState([]);
    const [animalSelected, setAnimalSelected] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;

    useEffect(() => {
        if (isLoading) {
            // get Animals
            getAllAnimalsWithLabel(false, true, (animals) => {
                setAnimals(animals);
                setAnimalSelected(animals[0].value);
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

        if (form.checkValidity()) {
            let milking = {
                liters: liters,
                animalMilked: Number.parseInt(animalSelected)
            }
            registerMilking(milking, () => {
                setSuccess('Milking created correctly');
                setAnimalSelected('');
                setLiters(1);
            }, errors => setBackendErrors(errors));
        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
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
                <div className="card bg-light border-dark">
                    <h5 className="card-header card-title-custom">
                        <FormattedMessage id="project.milking.create"></FormattedMessage>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                              className="needs-validation" noValidate
                              onSubmit={event => handleSubmit(event)}>
                            <div className="form-group row">
                                <label htmlFor="animal" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.milking.animal"/>
                                </label>
                                <div className="col-md-6">
                                    <select id="animal" className="form-control" required
                                            value={animalSelected}
                                            onChange={e => setAnimalSelected(e.target.value)}>
                                        {animals && animals.map(a =>
                                            <option key={a.value} value={a.value}>{a.label}</option>
                                        )}
                                    </select>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id="porject.global.validator.required"/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="liters" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.milking.liters"></FormattedMessage>
                                </label>
                                <div className="col-md-6">
                                    <input type="number" min="1"
                                           value={liters}
                                           required
                                           id="liters"
                                           className="form-control"
                                           onChange={e => setLiters(Number(e.target.value))}/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id="project.global.validator.required"></FormattedMessage>
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

export default CreateMilking;