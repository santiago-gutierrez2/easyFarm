import * as actionTypes from './actionTypes';
import backend from "../../backend";

const getAllIssuesCompleted = issuesSearch  => ({
    type: actionTypes.FIND_ALL_ISSUES_COMPLETED,
    issuesSearch
});

const clearIssues = () => ({
    type: actionTypes.CLEAR_ISSUES
})

export const getAllIssues = (criteria, onSuccess, onErrors) => dispacth => {
    dispacth(clearIssues());
    backend.issueService.getAllIssues(criteria,
        result => {
            dispacth(getAllIssuesCompleted({criteria, result}));
            onSuccess(result);
        }, error => {
            console.log(error);
            onErrors(error);
        }
    );
}

export const previousGetAllIssues = criteria => {
    getAllIssues({...criteria, page: criteria.page - 1});
}

export const nextGetAllIssues = criteria => {
    getAllIssues({...criteria, page: criteria.page + 1});
}