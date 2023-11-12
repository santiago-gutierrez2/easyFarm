import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';
import {useEffect} from "react";

import {Errors} from '../../common';
import {Success} from "../../common";
import users from "../../users";
import {useState} from "react";
import {getEmployees} from "../../../backend/userService";
import {createIssue} from "../../../backend/issueService";

const CreateIssue = () => {

    const history = useHistory();
    const user = useSelector(users.selectors.getUser);
    const [employees, setEmployees] = useState([]);
    const [issueName, setIssueName] = useState('');
    const [description, setDescription] = useState('');
    const [assignedTo, setAssignedTo] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;

    useEffect(async () => {
        //await sleep(2000)
        if (isLoading) {
            getEmployees((employeesDTO) => {
                setEmployees(employeesDTO);
                setAssignedTo(employeesDTO[0].id);
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
        }

    }, [isLoading, employees]);

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            let issue = {
                issueName: issueName.trim(),
                description: description != null ? description.trim() : null,
                creationDate: 0,
                isDone: false,
                createdBy: Number.parseInt(user.id),
                assignedTo: Number.parseInt(assignedTo)
            }
            console.log(assignedTo);
            console.log(issue);
            createIssue(issue,
                () => {
                    setSuccess('Issue created correctly');
                    setIssueName('');
                    setDescription('');
                },
                errors => setBackendErrors(errors)
            )
        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }

    }


    return (
        <div className="row justify-content-center">
            <div className="col-sm-7 col-12">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}></Success>
                <div className="card bg-light border-dark">
                    <h5 className="card-header card-title-custom">
                        <FormattedMessage id="project.issues.create"/>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                            className="needs-validation" noValidate
                            onSubmit={e => handleSubmit(e)}>
                            <div className="form-group row">
                                <label htmlFor="issueName" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.issues.issueName"/>
                                </label>
                                <div className="col-md-6">
                                    <input type="text" id="issueName" className="form-control"
                                       value={issueName}
                                       onChange={e => setIssueName(e.target.value)}
                                       autoFocus
                                       required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="description" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.issues.description" />
                                </label>
                                <div className="col-md-6">
                                    <textarea className="form-control" id="description" rows="3"
                                        value={description}
                                        onChange={e => setDescription(e.target.value)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="assignedTo" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.issues.assignedTo"/>
                                </label>
                                <div className="col-md-6">
                                    <select id="assignedTo" className="form-control" required
                                        value={assignedTo}
                                        onChange={e => setAssignedTo(e.target.value)}>
                                        {employees && employees.map(employee =>
                                        <option key={employee.id} value={employee.id}>{employee.userName}</option>
                                        )}
                                    </select>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
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

export default CreateIssue;