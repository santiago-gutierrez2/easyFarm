import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import {Success} from "../../common";
import * as actions from '../actions';
import users from "../index";
import * as commonActions from "../../app/actions";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import {BreadcrumbItem} from "react-bootstrap";

const SignUp = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const user = useSelector(users.selectors.getUser); // admin user (creator) to get info of farm.
    const [userName, setUserName] = useState('');
    const [dni, setDni] = useState('');
    const [nss, setNss] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail]  = useState('');
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState(false);
    let form;
    let confirmPasswordInput;
    // set active header item.
    dispatch(commonActions.activeItem('Item1'));

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity() && checkConfirmPassword()) {
            
            dispatch(actions.signUp(
                {userName: userName.trim(),
                dni: dni.trim(),
                nss: nss.trim(),
                password: password,
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                email: email.trim(),
                farm: user.farm},
                () =>{
                    //history.push('/')
                    setSuccess('Employee created correctly');
                    // reset state of form
                    setUserName('');
                    setDni('');
                    setNss('');
                    setPassword('');
                    setConfirmPassword('');
                    setFirstName('');
                    setLastName('');
                    setEmail('');
                },
                errors => setBackendErrors(errors),
                () => {
                    history.push('/users/login');
                    dispatch(actions.logout());
                }
            ));
            

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }

    }

    const checkConfirmPassword = () => {

        if (password !== confirmPassword) {

            confirmPasswordInput.setCustomValidity('error');
            setPasswordsDoNotMatch(true);

            return false;

        } else {
            return true;
        }

    }

    const handleConfirmPasswordChange = value => {

        confirmPasswordInput.setCustomValidity('');
        setConfirmPassword(value);
        setPasswordsDoNotMatch(false);
    
    }

    return (
        <>
            <Breadcrumb>
                <Breadcrumb.Item href="/">Dashboard</Breadcrumb.Item>
                <Breadcrumb.Item href="/users/seeAllEmployees">Employees list</Breadcrumb.Item>
                <Breadcrumb.Item active>Create</Breadcrumb.Item>
            </Breadcrumb>
            <div className="row justify-content-center fade-in">
                <div className="col-sm-7 col-12">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <Success message={success} onClose={() => setSuccess(null)}></Success>
                    <div className="card bg-light">
                        <h5 className="card-header card-title-custom">
                            <FormattedMessage id="project.users.employees.create"/>
                        </h5>
                        <div className="card-body">
                            <form ref={node => form = node}
                                  className="needs-validation" noValidate
                                  onSubmit={e => handleSubmit(e)}>
                                <div className="form-group row">
                                    <label htmlFor="dni" className="col-md-3 col-form-label">
                                        DNI
                                    </label>
                                    <div className="col-md-6">
                                        <input type="text" id="dni" className="form-control"
                                               value={dni}
                                               onChange={e => setDni(e.target.value)}
                                               autoFocus
                                               required/>
                                    </div>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="nss" className="col-md-3 col-form-label">
                                        N.S.S.
                                    </label>
                                    <div className="col-md-6">
                                        <input type="text" id="nss" className="form-control"
                                               value={nss}
                                               onChange={e => setNss(e.target.value)}
                                               required/>
                                    </div>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="userName" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.global.fields.userName"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input type="text" id="userName" className="form-control"
                                               value={userName}
                                               onChange={e => setUserName(e.target.value)}
                                               required/>
                                        <div className="invalid-feedback">
                                            <FormattedMessage id='project.global.validator.required'/>
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="password" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.global.fields.password"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input type="password" id="password" className="form-control"
                                               value={password}
                                               onChange={e => setPassword(e.target.value)}
                                               required/>
                                        <div className="invalid-feedback">
                                            <FormattedMessage id='project.global.validator.required'/>
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="confirmPassword" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.users.SignUp.fields.confirmPassword"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input ref={node => confirmPasswordInput = node}
                                               type="password" id="confirmPassword" className="form-control"
                                               value={confirmPassword}
                                               onChange={e => handleConfirmPasswordChange(e.target.value)}
                                               required/>
                                        <div className="invalid-feedback">
                                            {passwordsDoNotMatch ?
                                                <FormattedMessage id='project.global.validator.passwordsDoNotMatch'/> :
                                                <FormattedMessage id='project.global.validator.required'/>}
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="firstName" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.global.fields.firstName"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input type="text" id="firstName" className="form-control"
                                               value={firstName}
                                               onChange={e => setFirstName(e.target.value)}
                                               required/>
                                        <div className="invalid-feedback">
                                            <FormattedMessage id='project.global.validator.required'/>
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="lastName" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.global.fields.lastName"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input type="text" id="lastName" className="form-control"
                                               value={lastName}
                                               onChange={e => setLastName(e.target.value)}
                                               required/>
                                        <div className="invalid-feedback">
                                            <FormattedMessage id='project.global.validator.required'/>
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group row">
                                    <label htmlFor="email" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.global.fields.email"/>
                                    </label>
                                    <div className="col-md-6">
                                        <input type="email" id="email" className="form-control"
                                               value={email}
                                               onChange={e => setEmail(e.target.value)}
                                               required/>
                                        <div className="invalid-feedback">
                                            <FormattedMessage id='project.global.validator.email'/>
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

export default SignUp;
