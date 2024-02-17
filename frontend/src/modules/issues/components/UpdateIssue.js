import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory, useParams} from 'react-router-dom';
import {useEffect} from "react";

import {Errors} from '../../common';
import {Success} from "../../common";
import users from "../../users";
import {useState} from "react";
import {deleteIssue, getIssueById, updateIssue} from "../../../backend/issueService";
import * as commonActions from "../../app/actions";
import Modal from "react-bootstrap/Modal";
import {Button} from "react-bootstrap";

const UpdateIssue = () => {

    const {issueId} = useParams();
    const [editing, setEditing] = useState(false);
    const user = useSelector(users.selectors.getUser);
    const [issue, setIssue] = useState(null);
    const [issueName, setIssueName] = useState('');
    const [description, setDescription] = useState('');
    const [isDone, setIsDone] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    const dispatch = useDispatch();
    const [show, setShow] = useState(false);
    const history = useHistory();
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    let form;
    // set active header item.
    dispatch(commonActions.activeItem('Item2'));

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
                    setEditing(false);
                }, errors => setBackendErrors(errors)
            );
        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }
    }

    function handleDelete(issueId) {
        deleteIssue(issueId, () => {
            console.log('deleted issue correctly');
            setShow(false);
            history.push('/issues/SeeAllIssues');
        }, errors => {
            setShow(false);
            setBackendErrors(errors);
        });
    }

    return (
        <div className="row justify-content-center fade-in">
            <div className="col-sm-8 col-12">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                <Success message={success} onClose={() => setSuccess(null)}></Success>
                <div className="card bg-light ">
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
                                <div className="col-md-7">
                                    <input type="text" id="issueName" className="form-control"
                                           value={issueName}
                                           disabled={!editing}
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
                                <div className="col-md-7">
                                    <textarea className="form-control" id="description" rows="3"
                                              value={description}
                                              disabled={!editing}
                                              onChange={e => setDescription(e.target.value)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label htmlFor="isDone" className="col-md-3 col-form-label">
                                    <FormattedMessage id="project.issue.state" />
                                </label>
                                <div className="col-md-7">
                                    <div className="btn-group btn-group-toggle" data-toggle="buttons">
                                        <label className="btn btn-success active">
                                            <input type="radio" name="active" id="active" autoComplete="off" checked={!isDone}
                                                   disabled={!editing}
                                                   onClick={e => setIsDone(false)}/>
                                            <FormattedMessage id="project.issue.active"/>
                                        </label>
                                        <label className="btn btn-success">
                                            <input type="radio" name="done" id="done" autoComplete="off" checked={isDone}
                                                   disabled={!editing}
                                                   onClick={e => setIsDone(true)}/>
                                            <FormattedMessage id="project.issue.done" />
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="form-group row">
                                {editing &&
                                    <div className="offset-md-3 col-md-4">
                                        <button type="submit" className="btn btn-primary">
                                            <FormattedMessage id="project.global.update"/>
                                        </button>
                                        <button onClick={e => setEditing(false)}
                                                className="btn btn-danger ml-1">
                                            <FormattedMessage id="project.global.buttons.cancel"/>
                                        </button>
                                    </div>
                                }
                                {!editing &&
                                    <>
                                        <div className="offset-md-3 col-md-3">
                                            <button onClick={e => setEditing(true)}
                                                    className="btn btn-primary">
                                                <FormattedMessage id="project.global.edit"/> <i
                                                className="fas fa-pen"></i>
                                            </button>
                                            <button type="button" className="btn btn-danger ml-1" onClick={() => handleShow()}>
                                                <FormattedMessage id="project.global.delete"/> <i className="fas fa-trash"></i>
                                            </button>
                                            <Modal show={show} onHide={handleClose}>
                                                <Modal.Header>
                                                    <Modal.Title>Delete issue</Modal.Title>
                                                </Modal.Header>
                                                <Modal.Body>Are you sure to delete this issue?</Modal.Body>
                                                <Modal.Footer>
                                                    <Button variant="secondary" onClick={handleClose}>
                                                        Cancel
                                                    </Button>
                                                    <Button variant="danger" onClick={() => handleDelete(issue.id)}>
                                                        Delete
                                                    </Button>
                                                </Modal.Footer>
                                            </Modal>
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

export default UpdateIssue;