import {useState} from "react";
import {FormattedMessage} from "react-intl";
import {Errors} from "../../common";
import {useEffect} from "react";
import {deleteEmployee, getEmployees} from "../../../backend/userService";
import {BounceLoader, MoonLoader, PuffLoader} from "react-spinners";
import {useDispatch} from "react-redux";
import * as commonActions from "../../app/actions";
import {useHistory} from "react-router-dom";
import Modal from "react-bootstrap/Modal";
import {Button} from "react-bootstrap";


const GetEmployees = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [employees, setEmployees] = useState([]);
    const [backendErrors, setBackendErrors] = useState(null);
    const dispatch = useDispatch();
    const [show, setShow] = useState(false);
    const history = useHistory();
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    // set active header item.
    dispatch(commonActions.activeItem('Item1'));

    useEffect(async () => {
        if (isLoading) {
            //await sleep(2000)
            getEmployees((employeesDTO) => {
                setEmployees(employeesDTO);
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
        }
    }, [isLoading, employees]);

    function handleDelete(employee) {
        console.log(employee);
        setIsLoading(true);
        deleteEmployee(employee.id,
            () => {
                setEmployees(employees.filter(emp => emp.id !== employee.id));
                setIsLoading(false);
            },
            errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
    }

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <PuffLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
        <div className="row justify-content-center fade-in">
            <div className="col-12 col-sm-10">
                <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}></Errors>
                <div className="table-responsive">
                    <table className="table">
                        <thead className="thead-dark">
                        <tr>
                            <th scope="col"><FormattedMessage id="project.global.fields.userName"/></th>
                            <th scope="col"><FormattedMessage id="project.global.fields.firstName"/></th>
                            <th scope="col"><FormattedMessage id="project.global.fields.lastName"/></th>
                            <th scope="col"><FormattedMessage id="project.global.fields.email"/></th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        {employees.map(employee => {
                            return (
                                <tr key={employee.id}>
                                    <td scope="row">{employee.userName}</td>
                                    <td scope="row">{employee.firstName}</td>
                                    <td scope="row">{employee.lastName}</td>
                                    <td scope="row">{employee.email}</td>
                                    <td scope="row">
                                        <button className="btn btn-danger" title="Delete" onClick={() => handleShow()}>
                                            Delete <i className="fas fa-trash"></i>
                                        </button>
                                        <Modal show={show} onHide={handleClose}>
                                            <Modal.Header>
                                                <Modal.Title>Delete employee</Modal.Title>
                                            </Modal.Header>
                                            <Modal.Body>Are you sure to delete this employee? deleting <b>wont affect</b> the data related to this employee</Modal.Body>
                                            <Modal.Footer>
                                                <Button variant="secondary" onClick={handleClose}>
                                                    Cancel
                                                </Button>
                                                <Button variant="danger" onClick={() => handleDelete(employee)}>
                                                    Delete
                                                </Button>
                                            </Modal.Footer>
                                        </Modal>
                                    </td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default GetEmployees;