import {useState} from "react";
import {FormattedMessage} from "react-intl";
import {Errors} from "../../common";
import {useEffect} from "react";
import {deleteEmployee, getEmployees} from "../../../backend/userService";
import {BounceLoader} from "react-spinners";


const GetEmployees = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [employees, setEmployees] = useState([]);
    const [backendErrors, setBackendErrors] = useState(null);

    const sleep = (milliseconds) => {
        return new Promise(resolve => setTimeout(resolve, milliseconds))
    }

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

    function handleDelete(event, employee) {
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
                    <BounceLoader color="#343A40" />
                </div>
            </div>
        );
    }

    return (
        <div className="row justify-content-center">
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
                                        <button className="btn btn-danger" title="Delete" onClick={event => handleDelete(event, employee)}>
                                            <i className="fas fa-trash"></i>
                                        </button>
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