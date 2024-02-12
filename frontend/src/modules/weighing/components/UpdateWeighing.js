import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {getWeighingById, updateWeighing} from "../../../backend/weighingService";
import {MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import {useDispatch} from "react-redux";
import * as commonActions from "../../app/actions";

const UpdateWeighing = () => {

    const {weighingId} = useParams();
    const [editing, setEditing] = useState(false);
    const [weighing, setWeighing] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [animalSelected, setAnimalSelected] = useState('');
    const [animals, setAnimals] = useState([]);
    const [kilos, setKilos] = useState(1);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;
    const dispatch = useDispatch();
    // set active header item.
    dispatch(commonActions.activeItem('Item6'));

    useEffect(() => {
        const id = Number(weighingId);
        if (isLoading) {
            // get weighing info
            getWeighingById(id, (weighing) => {
                setWeighing(weighing);
                setKilos(weighing.kilos);
                setAnimalSelected(weighing.animalWeighed);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
            // getAnimals
            getAllAnimalsWithLabel(false, false, (animals) => {
                setAnimals(animals);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
        }
    }, [isLoading, weighing]);

    function cancelUpdate() {
        setKilos(weighing.kilos);
        setEditing(false);
    }

    function getAnimaLabel(animalId) {
        for (let animal of animals) {
            if (animal.value === animalId) {
                return animal.label;
            }
        }
    }

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let weighingDTO = {
                kilos: kilos,
                animalWeighed: Number.parseInt(animalSelected)
            }
            updateWeighing(Number(weighingId), weighingDTO,
                () => {
                    var newWeighing = {...weighing};
                    newWeighing.kilos = kilos;
                    setWeighing(newWeighing);
                    setSuccess('Weighing updated Correctly');
                    setEditing(false);
                }, errors => {
                    setBackendErrors(errors);
                }
            );
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
                <div className="card bg-light ">
                    <h5 className="card-header card-title-custom">
                        <FormattedMessage id="project.weighing.update"/>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                              className="needs-validation" noValidate
                              onSubmit={e => handleSubmit(e)}>
                            <div className="form-group row">
                                <label htmlFor="animal" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.weighing.animal"/>
                                </label>
                                <div className="col-md-7">
                                    <p style={{marginTop: '8px'}}>
                                        <Link className="link" to={`/animal/${animalSelected}`}>
                                            {getAnimaLabel(animalSelected)}
                                        </Link>
                                    </p>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id="project.global.validator.required"/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="kilos" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.weighing.kilos"/>
                                </label>
                                <div className="col-md-7">
                                    <input type="number" min="1"
                                           value={kilos}
                                           disabled={!editing}
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
                                {editing &&
                                    <div className="offset-md-3 col-md-3">
                                        <button type="submit" className="btn btn-primary">
                                            <FormattedMessage id="project.global.update"/>
                                        </button>
                                        <button onClick={e => {setEditing(false); cancelUpdate();}}
                                            className="btn btn-danger ml-1">
                                            <FormattedMessage id="project.global.buttons.cancel"/>
                                        </button>
                                    </div>
                                }
                                {!editing &&
                                    <>
                                        <div className="offset-md-3 col-md-2">
                                            <button onClick={e => setEditing(true)}
                                                    className="btn btn-primary">
                                                <FormattedMessage id="project.global.edit"/>
                                            </button>
                                        </div>
                                    </>
                                }
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default UpdateWeighing;