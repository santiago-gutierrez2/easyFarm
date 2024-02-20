import {useHistory, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {deleteAnimal, getAnimalById, updateAnimal} from "../../../backend/animalService";
import {BounceLoader, MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import DatePicker from "react-datepicker";
import {AnimalFoodConsumptionChart, AnimalMilkingChart, AnimalWeighingChart} from "../../charts";
import {useDispatch, useSelector} from "react-redux";
import * as commonActions from "../../app/actions";
import users from "../../users";
import Modal from "react-bootstrap/Modal";
import {Button} from "react-bootstrap";
import Breadcrumb from 'react-bootstrap/Breadcrumb';


const UpdateAnimal = () => {

    const {animalId} = useParams();
    const history = useHistory();
    const role = useSelector(users.selectors.getRole);
    const [editing, setEditing] = useState(false);
    const [activeItem, setActiveItem] = useState('DATA');
    const [animal, setAnimal] = useState(null);
    const [name, setName] = useState('');
    const [identificationNumber, setIdentificationNumber] = useState(0);
    const [isMale, setIsMale] = useState(false);
    const [physicalDescription, setPhysicalDescription] = useState('');
    const [birthDateString, setBirthDateString] = useState(new Date());
    const [isDead, setIsDead] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const dispatch = useDispatch();
    // set active header item.
    dispatch(commonActions.activeItem('Item4'));

    useEffect(() => {
        const id = Number(animalId);
        if (isLoading) {
            getAnimalById(id, (animalDto) => {
                setAnimal(animalDto);
                console.log(animalDto);
                // set state for the form
                setName(animalDto.name);
                setIdentificationNumber(animalDto.identificationNumber);
                setBirthDateString(new Date(animalDto.birthDate));
                setIsMale(animalDto.male);
                setPhysicalDescription(animalDto.physicalDescription);
                setIsDead(animalDto.isDead);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
        }
    }, [isLoading, animal]);

    function cancelUpdate() {
        setName(animal.name);
        setIdentificationNumber(animal.identificationNumber);
        setBirthDateString(new Date(animal.birthDate));
        setPhysicalDescription(animal.physicalDescription);
        setIsDead(animal.isDead);
        setEditing(false);
    }

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let animalUpdate = {
                name: name.trim(),
                identificationNumber: identificationNumber,
                birthDateString: encodeURIComponent(birthDateString.toISOString().substring(0,10)),
                isMale: isMale,
                physicalDescription: physicalDescription != null ? physicalDescription.trim() : null,
                isDead: animal.dead
            }
            console.log(animalUpdate);
            updateAnimal(animalId, animalUpdate,
                (animalDto) => {
                    setAnimal(animalDto);
                    console.log(animalDto);
                    // set state for the form
                    setName(animalDto.name);
                    setIdentificationNumber(animalDto.identificationNumber);
                    setBirthDateString(new Date(animalDto.birthDate));
                    setIsMale(animalDto.male);
                    setPhysicalDescription(animalDto.physicalDescription);
                    setIsDead(animalDto.isDead);
                    setSuccess('Animal updated correctly.');
                    setEditing(false);
                }, errors => setBackendErrors(errors)
            );
        } else {
            setBackendErrors(null);
            form.classList.add('war-validated');
        }
    }

    function handleDelete(animalId) {
        deleteAnimal(animalId, () => {
            console.log('se borro');
            setShow(false);
            setIsLoading(true);
            //history.push("/animal/allAnimals");
        }, errors => {
            setShow(false);
            setBackendErrors(errors);
        })
    }

    function setAnimalAvailable() {
        console.log('set animal available');
        let animalUpdate = {
            name: name.trim(),
            identificationNumber: identificationNumber,
            birthDateString: encodeURIComponent(birthDateString.toISOString().substring(0,10)),
            isMale: isMale,
            physicalDescription: physicalDescription != null ? physicalDescription.trim() : null,
            isDead: false
        }
        updateAnimal(animalId, animalUpdate, (animalDto) => {
            setAnimal(animalDto);
            console.log(animalDto);
            setSuccess('Animal updated correctly.');
            setEditing(false);
        }, errors => setBackendErrors(errors));
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
                <Breadcrumb.Item href="/animal/allAnimals">Livestock list</Breadcrumb.Item>
                <Breadcrumb.Item active>{animal.name}-{animal.identificationNumber}</Breadcrumb.Item>
            </Breadcrumb>
            <div className="row justify-content-center">
                <ul className="nav nav-tabs col-sm-8 col-12" id="myTab" role="tablist">
                    <li className="nav-item" role="presentation">
                        <button className={activeItem === "DATA" ? "nav-link active" : "nav-link "} id="home-tab"
                                data-bs-toggle="tab" data-bs-target="#home"
                                type="button" role="tab" aria-controls="home" aria-selected="true"
                                onClick={e => setActiveItem('DATA')}>
                            {activeItem === 'DATA' &&
                                <b>Data</b>
                            }
                            {activeItem !== 'DATA' &&
                                <span>Data</span>
                            }
                        </button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button className={activeItem === "CHART" ? "nav-link active" : "nav-link "} id="profile-tab"
                                data-bs-toggle="tab" data-bs-target="#profile"
                                type="button" role="tab" aria-controls="profile" aria-selected="false"
                                onClick={e => {
                                    setActiveItem('CHART');
                                    cancelUpdate();
                                }}>
                            {activeItem === 'CHART' &&
                                <b>Stats</b>
                            }
                            {activeItem !== 'CHART' &&
                                <span>Stats</span>
                            }
                        </button>
                    </li>
                </ul>
            </div>

            <div className="tab-content" id="myTabContent">
                <div id="home" className={activeItem === "DATA" ? "tab-pane active" : "tab-pane"} role="tabpanel">
                    <div className="row justify-content-center fade-in mt-3">
                        <div className="col-sm-8 col-12">
                            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                            <Success message={success} onClose={() => setSuccess(null)}/>
                            <div className="card bg-light ">
                                <div className="card-header card-title-custom">
                                    <div className="row">
                                        <div className="col-xl-10 col-7">
                                            <h5>
                                                <FormattedMessage id="project.animal.update"/>
                                            </h5>
                                        </div>
                                        <div className="col-xl-2 col-5 align-self-center">
                                            <h3>
                                                {animal.dead &&
                                                    <span className="badge badge-danger float-right">
                                                        Unavailable
                                                    </span>
                                                }
                                            </h3>
                                        </div>
                                    </div>
                                </div>
                                <div className="card-body">
                                <form ref={node => form = node}
                                          className="needs-validation" noValidate
                                          onSubmit={event => handleSubmit(event)}>
                                        <div className="form-group row">
                                            <label htmlFor="name" className="col-md-3 col-form-label">
                                                <FormattedMessage id="project.animal.name"/>
                                            </label>
                                            <div className="col-md-7">
                                                <input type="text" id="name" className="form-control"
                                                       value={name}
                                                       disabled={!editing}
                                                       onChange={e => setName(e.target.value)}
                                                       autoFocus
                                                       required/>
                                                <div className="invalid-feedback">
                                                    <FormattedMessage id="project.global.validator.required"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="form-group row">
                                            <label htmlFor="identificationNumber" className="col-md-3 col-form-label">
                                                <FormattedMessage id="project.animal.identificationNumber"/>
                                            </label>
                                            <div className="col-md-7">
                                                <input type="number" id="identificationNumber" className="form-control"
                                                       value={identificationNumber}
                                                       disabled={!editing}
                                                       pattern="[0-9]{10}"
                                                       required
                                                       onChange={e => setIdentificationNumber(Number(e.target.value))}/>
                                            </div>
                                            <div className="invalid-feedback">
                                                <FormattedMessage id="project.global.validator.required"/>
                                            </div>
                                        </div>
                                        <div className="form-group row">
                                            <label htmlFor="physicalDescription" className="col-md-3 col-form-label">
                                                <FormattedMessage id="project.animal.physicalDescription"/>
                                            </label>
                                            <div className="col-md-7">
                                    <textarea className="form-control" id="physicalDescription" rows="3"
                                              value={physicalDescription}
                                              disabled={!editing}
                                              onChange={e => setPhysicalDescription(e.target.value)}>
                                    </textarea>
                                            </div>
                                        </div>
                                        <div className="form-group row">
                                            <label htmlFor="birthDate" className="col-md-3 col-form-label">
                                                <FormattedMessage id="project.animal.birthDate"/>
                                            </label>
                                            <div className="col-md-7">
                                                <DatePicker disabled={!editing} className="form-control"
                                                            selected={birthDateString}
                                                            onChange={(date) => setBirthDateString(date)}/>
                                            </div>
                                        </div>
                                        <div className="form-group row">
                                            <label htmlFor="isMale" className="col-md-3 col-form-label">
                                                <FormattedMessage id="project.animal.gender"/>
                                            </label>
                                            <div className="col-md-7">
                                                {isMale ?
                                                    <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                                        <label id="female" className="btn btn-success"
                                                               style={{zIndex: 0}}>
                                                            <input type="radio" name="male"
                                                                   id="femaleInput"
                                                                   disabled={!editing}
                                                                   autoComplete="off"
                                                                   onChange={e => setIsMale(false)}/>
                                                            <FormattedMessage id="project.animal.female"/>
                                                        </label>
                                                        <label id="male" className="btn btn-success active"
                                                               style={{zIndex: 0}}>
                                                            <input type="radio" name="female"
                                                                   id="maleInput"
                                                                   disabled={!editing}
                                                                   autoComplete="off"
                                                                   onChange={e => setIsMale(true)}/>
                                                            <FormattedMessage id="project.animal.male"/>
                                                        </label>
                                                    </div>
                                                    :
                                                    <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                                        <label id="female" className="btn btn-success active"
                                                               style={{zIndex: 0}}>
                                                            <input type="radio" name="male"
                                                                   id="femaleInput"
                                                                   disabled={!editing}
                                                                   autoComplete="off"
                                                                   onChange={e => setIsMale(false)}/>
                                                            <FormattedMessage id="project.animal.female"/>
                                                        </label>
                                                        <label id="male" className="btn btn-success"
                                                               style={{zIndex: 0}}>
                                                            <input type="radio" name="female"
                                                                   id="maleInput"
                                                                   disabled={!editing}
                                                                   autoComplete="off"
                                                                   onChange={e => setIsMale(true)}/>
                                                            <FormattedMessage id="project.animal.male"/>
                                                        </label>
                                                    </div>
                                                }
                                            </div>
                                        </div>
                                        <div className="form-group row">
                                            {editing &&
                                                <div className="offset-md-3 col-md-2">
                                                    <button type="submit" className="btn btn-primary">
                                                        <FormattedMessage id="project.global.update"/>
                                                    </button>
                                                    <button onClick={e => {setEditing(false); cancelUpdate();}}
                                                            className="btn btn-danger ml-1">
                                                        <FormattedMessage id="project.global.buttons.cancel"/>
                                                    </button>
                                                </div>
                                            }
                                            {!editing && role === "ADMIN" && !animal.dead &&
                                                <>
                                                    <div className="offset-md-3 col-md-3">
                                                        <button onClick={e => setEditing(true)}
                                                                className="btn btn-primary">
                                                            <FormattedMessage id="project.global.edit"/> <i className="fas fa-pen"></i>
                                                        </button>
                                                        <button type="button" className="btn btn-danger ml-1" onClick={() => handleShow()}>
                                                            Set unavailable
                                                        </button>

                                                        <Modal show={show} onHide={handleClose}>
                                                            <Modal.Header>
                                                                <Modal.Title>Set unavailable livestock</Modal.Title>
                                                            </Modal.Header>
                                                            <Modal.Body>Are you sure to set unavailable this animal? it <b>wont affect</b> the rest of the data related to this animal</Modal.Body>
                                                            <Modal.Footer>
                                                                <Button variant="secondary" onClick={handleClose}>
                                                                    Cancel
                                                                </Button>
                                                                <Button variant="danger" onClick={() => handleDelete(animal.id)}>
                                                                    Set Unavailable
                                                                </Button>
                                                            </Modal.Footer>
                                                        </Modal>
                                                    </div>
                                                </>
                                            }
                                            {!editing && role === "ADMIN" && animal.dead &&
                                                <>
                                                <div className="offset-md-3 col-md-2">
                                                    <button type="button" className="btn btn-primary" onClick={() => setAnimalAvailable(animalId)}>
                                                        Set animal available
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
                </div>

                <div className={activeItem === "CHART" ? "tab-pane active" : "tab-pane"} id="profile" role="tabpanel"
                     aria-labelledby="profile-tab">
                    <div className="row justify-content-center">
                        <div className="col-sm-8 col-12">
                            <AnimalFoodConsumptionChart/>
                        </div>
                    </div>

                    <div className="row justify-content-center mt-2">
                        <div className="col-sm-8 col-12">
                            <AnimalWeighingChart/>
                        </div>
                    </div>

                    {!isMale &&
                        <div className="row justify-content-center mt-2">
                            <div className="col-sm-8 col-12">
                                <AnimalMilkingChart/>
                            </div>
                        </div>
                    }
                </div>
            </div>
        </>
    );
}

export default UpdateAnimal;