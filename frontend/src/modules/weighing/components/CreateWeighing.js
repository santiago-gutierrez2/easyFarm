import {useEffect, useState} from "react";
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedDate, FormattedMessage} from "react-intl";
import {registerWeighing} from "../../../backend/weighingService";
import {useDispatch} from "react-redux";
import * as commonActions from "../../app/actions";
import Breadcrumb from "react-bootstrap/Breadcrumb";

const CreateWeighing = () => {

    const [kilos, setKilos] = useState(1);
    const [animals, setAnimals] = useState([]);
    const [animalSelected, setAnimalSelected] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;
    const dispatch = useDispatch();
    // set active header item.
    dispatch(commonActions.activeItem('Item6'));

    useEffect(() => {
        if (isLoading) {
            // get animals
            getAllAnimalsWithLabel(false, false,(animals) => {
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
            let weighing = {
                kilos: kilos,
                animalWeighed: Number.parseInt(animalSelected)
            }
            registerWeighing(weighing, () => {
                setSuccess('Weighing created correctly');
                setAnimalSelected('');
                setKilos(1);
            }, errors => setBackendErrors(errors))
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
        <>
            <Breadcrumb>
                <Breadcrumb.Item href="/">Dashboard</Breadcrumb.Item>
                <Breadcrumb.Item href="/weighing/AllWeighings">Weighings list</Breadcrumb.Item>
                <Breadcrumb.Item active>Register</Breadcrumb.Item>
            </Breadcrumb>
            <div className="row justify-content-center fade-in">
                <div className="col-sm-7 col-12">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <Success message={success} onClose={() => setSuccess(null)}></Success>
                    <div className="card bg-light ">
                        <h5 className="card-header card-title-custom">
                            <FormattedMessage id="project.weighing.create"/>
                        </h5>
                        <div className="card-body">
                            <form ref={node => form = node}
                                  className="needs-validation" noValidate
                                  onSubmit={event => handleSubmit(event)}>
                                <div className="form-group row">
                                    <label htmlFor="animal" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.weighing.animal"/>
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
                                            <FormattedMessage id="project.global.validator.required"/>
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="kilos" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.weighing.kilos"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input type="number" min="1"
                                               value={kilos}
                                               required
                                               id="kilos"
                                               className="form-control"
                                               onChange={e => setKilos(Number(e.target.value))}/>
                                        <div className="invalid-feedback">
                                            <FormattedMessage id="project.global.validator.required"/>
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
        </>
    );
}

export default CreateWeighing;