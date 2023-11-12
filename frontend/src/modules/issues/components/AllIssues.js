import {useState} from "react";
import {FormattedDate, FormattedMessage} from "react-intl";
import {Errors, Pager} from "../../common";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {useEffect} from "react";
import {BounceLoader, MoonLoader} from "react-spinners";
import {useDispatch, useSelector} from "react-redux";
import * as actions from '../actions';
import * as selectors from '../selectors';
import {Link} from "react-router-dom";
import "./AllIssuesStyle.css"
import {getEmployees} from "../../../backend/userService";

const AllIssues = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [issueName, setIssueName] = useState('');
    const [isDone, setIsDone] = useState('');
    const [employees, setEmployees] = useState([]);
    const issuesSearch = useSelector(selectors.getIssuesSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);

    useEffect(async () => {
        if (isLoading) {
            await getEmployees((employeesDTO) => {
                setEmployees(employeesDTO);
                setBackendErrors(null);
            }, errors => {
                setBackendErrors(errors);
            });
            dispatch(actions.getAllIssues({page: 0},
                (result) => {
                    console.log(result);
                    setIsLoading(false);
                    setBackendErrors(null);
                }, (errors) => {
                    setBackendErrors(errors);
                    setIsLoading(false);
                })
            );

        }

    }, [isLoading, dispatch]);

    function getEmployeeUserName(employeeId) {
        const employee = employees.filter(e => e.id === employeeId);
        if (employee.length > 0) {
            return employee[0].userName;
        } else {
            return '';
        }
    }

    function handleFilter() {
        let criteria = {
            page: 0,
            issueName: issueName.length > 0 ? issueName : null,
            startDate: startDate != null ? startDate : null,
            endDate: endDate != null ? endDate : null,
            isDone: isDone === "" ? null : isDone
        }
        dispatch(actions.getAllIssues(criteria,
            (result) => {
                console.log(result);
            }, (errors) => {
                setBackendErrors(errors);
            })
        );
    }

    if (!issuesSearch) {
        return null;
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
      <div className="container">
          <div className="row mb-3">
              <div className="col-sm-4">
                  <label className="col-form-label"><FormattedMessage id="project.issues.issueName"/>: </label>
                  <input type="text" placeholder="Issue name" className="form-control" value={issueName}
                         onChange={e => setIssueName(e.target.value)}/>
              </div>
              <div className="col-sm-3">
                  <label className="col-form-label"><FormattedMessage id="project.issue.state" />: </label>
                  <select className="form-control" value={isDone} onChange={e => setIsDone(e.target.value)}>
                      <option key={0} value="">{/*<FormattedMessage id="project.global.all"/>*/}All</option>
                      <option key={1} value="false">{/*<FormattedMessage id="project.issue.active"/>*/}Active</option>
                      <option key={2} value="true">{/*<FormattedMessage id="project.issue.done" />*/}Done</option>
                  </select>
              </div>
              <div className="col-sm-3">
                <label className="col-form-label"><FormattedMessage id="project.issues.creationDate"/>: </label> <div className="w-100"></div>
                <DatePicker className="form-control" selected={startDate} onChange={(date) => setStartDate(date)} />
                <DatePicker className="form-control mt-1" selected={endDate} onChange={(date) => setEndDate(date)} />
              </div>
              <div className="col-sm-2 text-center">
                  <button className="btn btn-primary" style={{marginTop: '37px'}} onClick={e => handleFilter()}><FormattedMessage id="project.global.search"/></button>
              </div>
          </div>
          {issuesSearch.result.items.length === 0 &&
              <div className="alert alert-danger" role="alert">
                  <FormattedMessage id='project.issues.issuesNotFound'/>
              </div>
          }
          {issuesSearch.result.items.map(issue => {
              return (
                  <div key={issue.id} className="card mt-2">
                      <div className="card-header container card-title-custom">
                        <div className="row">
                            <div className="col-xl-10 col-7">
                                <h3>{issue.issueName}</h3>
                            </div>
                            <div className="col-xl-2 col-5">
                                {issue.done ?
                                    <button type="button" className="btn button-badge btn-danger button-badge disabled float-right">
                                        <FormattedMessage id="project.issue.done"/>
                                    </button>
                                    :
                                    <button type="button" className="btn button-badge btn-success button-badge disabled float-right">
                                        <FormattedMessage id="project.issue.active"/>
                                    </button>
                                }
                            </div>
                        </div>
                      </div>
                      <div className="card-body">
                          <p className="card-text">{issue.description}</p>
                          <p className="card-text"><FormattedMessage id="project.issues.creationDate"/>: <FormattedDate value={new Date(issue.creationDate)} /></p>
                          <p className="card-text"><FormattedMessage id="project.issues.assignedTo"/>: {getEmployeeUserName(issue.assignedTo)}</p>
                          {/*<div className="row">
                              <div className="col-6">*/}
                                  <Link className="btn btn-primary" to={`/issues/${issue.id}`}>
                                      <FormattedMessage id="project.issues.update"/>
                                  </Link>
                              {/*</div>
                          </div>*/}
                      </div>
                  </div>
              )
          })}
          <div className="row mt-4">
              <div className="col-12">
                  <Pager
                      back={{
                          enabled: issuesSearch.criteria.page >= 1,
                          onClick: () => dispatch(actions.previousGetAllIssues(issuesSearch.criteria))
                      }}
                      next={{
                          enabled: issuesSearch.result.existMoreItems,
                          onClick: () => dispatch(actions.nextGetAllIssues(issuesSearch.criteria))
                      }}/>
              </div>
          </div>
      </div>
    );
}

export default AllIssues;