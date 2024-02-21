import {useEffect, useState} from "react";
import {getMyActiveIssues, setIssueAsDone} from "../../../backend/issueService";
import {PuffLoader} from "react-spinners";
import {FormattedDate, FormattedMessage} from "react-intl";
import {Errors, Success} from "../../common";


const MyActiveIssues = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [issues, setIssues] = useState([]);
    const [backendErrors, setBackendErrors] = useState(null);
    const [showChart, setShowChart] = useState(true);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        if(isLoading) {
            getMyActiveIssues((issuesDto) => {
                setIssues(issuesDto);
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
        }
    }, [isLoading, issues]);

    const sleep = (milliseconds) => {
        return new Promise(resolve => setTimeout(resolve, milliseconds))
    }

    function handleSetAsDone(issueId) {
        setIssueAsDone(issueId, async () => {
            setIsLoading(true);
            setSuccess("Issue updated successfully.");
            await sleep(2000);
            setSuccess(null)
        }, errors => {
            setBackendErrors(errors);
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
        <div className="card card-chart">
            <div className="row">
                <div className="col-12 ml-4 mt-2 chart-title" onClick={e => setShowChart(!showChart)}>
                    {showChart &&
                        <h4><b>My active issues <i className="fas fa-caret-up"></i></b></h4>
                    }
                    {!showChart &&
                        <h4><b>My active issues <i className="fas fa-caret-down"></i></b></h4>
                    }
                </div>
            </div>
            {showChart &&
                <div className="row justify-content-center">
                    <div className="col-12 col-sm-10">
                        <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}></Errors>
                        <Success message={success} onClose={() => setSuccess(null)}></Success>
                        {issues.length === 0 &&
                            <div className="alert alert-primary" role="alert">
                                You don't have any associated issue.
                            </div>
                        }
                        {issues.length > 0 &&
                            <div className="table-responsive">
                                <table className="table">
                                    <thead className="thead-dark">
                                    <tr>
                                        <th scope="col"><FormattedMessage id="project.issues.issueName"/></th>
                                        <th scope="col"><FormattedMessage id="project.issues.creationDate"/></th>
                                        <th scope="col">Description</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {issues.map(issue => {
                                        return (
                                            <tr key={issue.id}>
                                                <td scope="row">{issue.issueName}</td>
                                                <td scope="row"><FormattedDate value={new Date(issue.creationDate)}/>
                                                </td>
                                                <td scope="row">{issue.description}</td>
                                                <td scope="row">
                                                    <button className="btn btn-success"
                                                            onClick={e => handleSetAsDone(issue.id)}>
                                                        Set as done
                                                    </button>
                                                </td>
                                            </tr>
                                        );
                                    })}
                                    </tbody>
                                </table>
                            </div>
                        }
                    </div>
                </div>
            }
        </div>
    );
}

export default MyActiveIssues;