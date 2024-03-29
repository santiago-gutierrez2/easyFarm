import {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import Breadcrumb from "react-bootstrap/Breadcrumb";

const UpdateProfile = () => {

    const user = useSelector(selectors.getUser);
    const dispatch = useDispatch();
    const history = useHistory();
    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [email, setEmail]  = useState(user.email);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.updateProfile(
                {id: user.id,
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                email: email.trim()},
                () => history.push('/'),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }

    }

    return (
        <>
            <Breadcrumb>
                <Breadcrumb.Item href="/">Dashboard</Breadcrumb.Item>
                <Breadcrumb.Item active>Update profile</Breadcrumb.Item>
            </Breadcrumb>
            <div className="row justify-content-center fade-in">
                <div className="col-sm-7 col-12">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div className="card bg-light">
                        <h5 className="card-header card-title-custom ">
                            <FormattedMessage id="project.users.UpdateProfile.title"/>
                        </h5>
                        <div className="card-body">
                            <form ref={node => form = node}
                                  className="needs-validation" noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group row">
                                    <label htmlFor="firstName" className="col-md-3 col-form-label">
                                        <FormattedMessage id="project.global.fields.firstName"/>
                                    </label>
                                    <div className="col-md-7">
                                        <input type="text" id="firstName" className="form-control"
                                               value={firstName}
                                               onChange={e => setFirstName(e.target.value)}
                                               autoFocus
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
                                    <div className="col-md-7">
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
                                    <div className="col-md-7">
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
                                    <div className="offset-md-3 col-md-1">
                                        <button type="submit" className="btn btn-primary">
                                            <FormattedMessage id="project.global.buttons.save"/>
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

export default UpdateProfile;
