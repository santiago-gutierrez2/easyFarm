import {useHistory} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import users from "../../users";
import {useState} from "react";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import DatePicker from "react-datepicker";
import {createAnimal} from "../../../backend/animalService";
import * as commonActions from "../../app/actions";


const CreateAnimal = () => {

    const history = useHistory();
    const [name, setName] = useState('');
    const [identificationNumber, setIdentificationNumber] = useState(0);
    const [birthDateString, setBirthDateString] = useState(new Date());
    const [isMale, setIsMale] = useState(false);
    const [physicalDescription, setPhysicalDescription] = useState('');
    const [isDead, setIsDead] = useState(false);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;
    const dispatch = useDispatch();
    // set active header item.
    dispatch(commonActions.activeItem('Item4'));

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let animal = {
                name: name.trim(),
                identificationNumber: identificationNumber,
                birthDateString: encodeURIComponent(birthDateString.toISOString().substring(0,10)),
                isMale: isMale,
                physicalDescription: physicalDescription != null ? physicalDescription.trim() : null,
                isDead: false
            }
            console.log(animal);
            createAnimal(animal,
                () => {
                    setSuccess('Animal registered correctly');
                    setName('');
                    setIdentificationNumber(0);
                    setBirthDateString(new Date());
                    setIsMale(false);
                    setPhysicalDescription('');

                }, errors => setBackendErrors(errors)
            );
        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }
    }

    return (
        <div className="row justify-content-center fade-in">
            <div className="col-sm-7 col-12">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}/>
                <div className="card bg-light">
                    <h5 className="card-header card-title-custom">
                        <FormattedMessage id="project.animal.create"></FormattedMessage>
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
                                    <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                        <label className="btn btn-success active" style={{zIndex: 0}}>
                                            <input type="radio" name="male"
                                                   id="male"
                                                   autoComplete="off"
                                                   checked
                                                   onChange={e => setIsMale(false)}/>
                                                <FormattedMessage id="project.animal.female"/>
                                        </label>
                                        <label className="btn btn-success" style={{zIndex: 0}}>
                                            <input type="radio" name="female"
                                                   id="female"
                                                   autoComplete="off"
                                                   onChange={e => setIsMale(true)}/>
                                            <FormattedMessage id="project.animal.male"/>
                                        </label>
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

export default CreateAnimal;