import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getAnimalById, updateAnimal} from "../../../backend/animalService";
import {BounceLoader, MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import DatePicker from "react-datepicker";


const UpdateAnimal = () => {

    const {animalId} = useParams();
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

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let animalUpdate = {
                name: name.trim(),
                identificationNumber: identificationNumber,
                birthDateString: encodeURIComponent(birthDateString.toISOString().substring(0,10)),
                isMale: isMale,
                physicalDescription: physicalDescription != null ? physicalDescription.trim() : null,
                isDead: false
            }
            console.log(animalUpdate);
            updateAnimal(animalId, animalUpdate,
                () => {
                    setSuccess('Animal updated correctly.');
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
                    <MoonLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
        <div className="row justify-content-center fade-in">
            <div className="col-sm-7 col-12">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}/>
                <div className="card bg-light ">
                    <h5 className="card-header card-title-custom">
                        <FormattedMessage id="project.animal.update"/>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                            className="needs-validation" noValidate
                            onSubmit={event => handleSubmit(event)}>
                            <div className="form-group row">
                                <label htmlFor="name" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.animal.name"/>
                                </label>
                                <div className="col-md-6">
                                    <input type="text" id="name" className="form-control"
                                           value={name}
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
                                <div className="col-md-6">
                                    <input type="number" id="identificationNumber" className="form-control"
                                           value={identificationNumber}
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
                                <div className="col-md-6">
                                    <textarea className="form-control" id="physicalDescription" rows="3"
                                              value={physicalDescription}
                                              onChange={e => setPhysicalDescription(e.target.value)}>
                                    </textarea>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="birthDate" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.animal.birthDate"/>
                                </label>
                                <div className="col-md-6">
                                    <DatePicker className="form-control" selected={birthDateString} onChange={(date) => setBirthDateString(date)} />
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="isMale" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.animal.gender"/>
                                </label>
                                <div className="col-md-6">
                                    {isMale ?
                                        <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                            <label id="female" className="btn btn-success" style={{zIndex: 0}}>
                                                <input type="radio" name="male"
                                                       id="femaleInput"
                                                       autoComplete="off"
                                                       onChange={e => setIsMale(false)}/>
                                                <FormattedMessage id="project.animal.female"/>
                                            </label>
                                            <label id="male" className="btn btn-success active" style={{zIndex: 0}}>
                                                <input type="radio" name="female"
                                                       id="maleInput"
                                                       autoComplete="off"
                                                       onChange={e => setIsMale(true)}/>
                                                <FormattedMessage id="project.animal.male"/>
                                            </label>
                                        </div>
                                    :
                                        <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                            <label id="female" className="btn btn-success active" style={{zIndex: 0}}>
                                                <input type="radio" name="male"
                                                       id="femaleInput"
                                                       autoComplete="off"
                                                       onChange={e => setIsMale(false)}/>
                                                <FormattedMessage id="project.animal.female"/>
                                            </label>
                                            <label id="male" className="btn btn-success" style={{zIndex: 0}}>
                                                <input type="radio" name="female"
                                                       id="maleInput"
                                                       autoComplete="off"
                                                       onChange={e => setIsMale(true)}/>
                                                <FormattedMessage id="project.animal.male"/>
                                            </label>
                                        </div>
                                    }
                                </div>
                            </div>
                            <div className="form-group row">
                                <div className="offset-md-3 col-md-2">
                                    <button type="submit" className="btn btn-primary">
                                        <FormattedMessage id="project.global.update"/>
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

export default UpdateAnimal;