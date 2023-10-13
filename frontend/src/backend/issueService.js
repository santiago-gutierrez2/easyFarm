import {config, appFetch, setServiceToken, getServiceToken, removeServiceToken, setReauthenticationCallback} from './appFetch';

export const createIssue = (issue, onSuccess, onErrors) => {
    appFetch('/issues/createIssue', config('POST', issue),
        onSuccess, onErrors);
}