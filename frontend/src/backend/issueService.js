import {config, appFetch, setServiceToken, getServiceToken, removeServiceToken, setReauthenticationCallback} from './appFetch';

export const createIssue = (issue, onSuccess, onErrors) => {
    appFetch('/issues/createIssue', config('POST', issue),
        onSuccess, onErrors);
}

export const updateIssue = (issueId, issue, onSuccess, onErrors) => {
    appFetch(`/issues/${issueId}`, config('PUT', issue),
        onSuccess, onErrors);
}

export const getIssueById = (issueId, onSuccess, onErrors) => {
    appFetch(`/issues/${issueId}`, config('GET'), onSuccess, onErrors);
}

export const deleteIssue = (issueId, onSuccess, onErrors) => {
    appFetch(`/issues/${issueId}/deleteIssue`, config('DELETE'), onSuccess, onErrors);
}

export const getAllIssues = ({page, issueName, startDate, endDate, isDone}, onSuccess, onErrors) => {

    let path = `/issues/allIssues?page=${page}&size=5`;

    path += issueName?.length > 0 ? `&issueName=${encodeURIComponent(issueName)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";
    path += isDone ? `&isDone=${encodeURIComponent(isDone)}` : "";

    appFetch(path, config('GET'),
        onSuccess, onErrors);
}