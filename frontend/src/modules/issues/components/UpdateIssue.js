import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory, useParams} from 'react-router-dom';
import {useEffect} from "react";

import {Errors} from '../../common';
import {Success} from "../../common";
import users from "../../users";
import {useState} from "react";
import {getIssueById, updateIssue} from "../../../backend/issueService";

const UpdateIssue = () => {

    const {issueId} = useParams();
    const user = useSelector(users.selectors.getUser);
    const history = useHistory();
    const [issue, setIssue] = useState(null);
    const [issueName, setIssueName] = useState('');
    const [description, setDescription] = useState('');
    const [isDone, setIsDone] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;

    useEffect(async () => {
        const id = Number(issueId);
        if (isLoading) {
            getIssueById(id, (issueDTO) => {
                setIssue(issueDTO);
                // set state of the form
                setIssueName(issueDTO.issueName);
                setDescription(issueDTO.description);
                setIsDone(issueDTO.done);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
        }
    }, [isLoading, issue]);

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            let issueDto = {
                issueName: issueName.trim(),
                description: description != null ? description.trim() : null,
                isDone: isDone,
                createdBy: user.id
            }
            console.log(issueDto);

            updateIssue(Number(issueId), issueDto,
                (issueDto) => {
                    setSuccess('Issue updated Correctly');
                    setIssue(issueDto);
                    // set state of the form
                    setIssueName(issueDto.issueName);
                    setDescription(issueDto.description);
                    setIsDone(issueDto.done);
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
                <Success message={success} onClose={() => setSuccess(null)}></Success>
                <div className="card bg-light border-dark">
                    <h5 className="card-header card-title-custom ">
                        <FormattedMessage id="project.issues.update"/>
                    </h5>
                    <div className="card-body">
                        <form ref={node => form = node}
                            className="needs-validation" noValidate
                            onSubmit={e => handleSubmit(e)}>
                            <div className="form-group row">
                                <label htmlFor="issueName" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.issues.issueName"/>
                                </label>
                                <div className="col-md-4">
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
                                <div className="col-md-4">
                                    <textarea className="form-control" id="description" rows="3"
                                              value={description}
                                              onChange={e => setDescription(e.target.value)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="isDone" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.issue.state" />
                                </label>
                                <div className="col-md-4">
                                    <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                        <label className="btn btn-info active">
                                            <input type="radio" name="active" id="active" autoComplete="off" checked={!isDone}
                                                   onClick={e => setIsDone(false)}/>
                                            <FormattedMessage id="project.issue.active"/>
                                        </label>
                                        <label className="btn btn-info">
                                            <input type="radio" name="done" id="done" autoComplete="off" checked={isDone}
                                                   onClick={e => setIsDone(true)}/>
                                            <FormattedMessage id="project.issue.done" />
                                        </label>
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
    );
}

export default UpdateIssue;