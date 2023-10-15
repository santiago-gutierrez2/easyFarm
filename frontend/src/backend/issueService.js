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

export const getAllIssues = ({page}, onSuccess, onErrors) => {
    appFetch(`/issues/allIssues?page=${page}&size=10`, config('GET'),
        onSuccess, onErrors);
}