import {useState} from "react";
import {FormattedDate, FormattedMessage} from "react-intl";
import {Errors} from "../../common";
import {useEffect} from "react";
import {BounceLoader} from "react-spinners";
import {useDispatch, useSelector} from "react-redux";
import * as actions from '../actions';
import * as selectors from '../selectors';
import {Link} from "react-router-dom";

const AllIssues = () => {

    const [isLoading, setIsLoading] = useState(true);
    const issuesSearch = useSelector(selectors.getIssuesSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);

    useEffect(() => {
        if (isLoading) {
            dispatch(actions.getAllIssues({page: 0},
                (result) => {
                    console.log(result);
                    setIsLoading(false);
                }, (errors) => {
                    setBackendErrors(errors);
                    setIsLoading(false);
                })
            );
        }

    }, [isLoading, dispatch])

    if (!issuesSearch) {
        return null;
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

    if (issuesSearch.result.items.length === 0) {
        return (
            <div className="alert alert-danger" role="alert">
                <FormattedMessage id='project.issues.issuesNotFound'/>
            </div>
        );
    }

    return (
      <div className="container">
          {issuesSearch.result.items.map(issue => {
              return (
                  <div className="card">
                      <div className="card-header container">
                        <div className="row">
                            <div className="col-xl-10 col-7">
                                <h2>{issue.issueName}</h2>
                            </div>
                            <div className="col-xl-2 col-5">
                                {issue.done ?
                                    <button type="button" className="btn btn-danger disabled float-right">
                                        <FormattedMessage id="project.issue.done"/>
                                    </button>
                                    :
                                    <button type="button" className="btn btn-success disabled float-right">
                                        <FormattedMessage id="project.issue.active"/>
                                    </button>
                                }
                            </div>
                        </div>
                      </div>
                      <div className="card-body">
                          <p className="card-text">{issue.description}</p>
                          <p className="card-text"><FormattedMessage id="project.issues.creationDate"/>: <FormattedDate value={new Date(issue.creationDate)} /></p>
                          <p className="card-text"><FormattedMessage id="project.issues.assignedTo"/>: {issue.assignedTo}</p>
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
      </div>
    );
}

export default AllIssues;