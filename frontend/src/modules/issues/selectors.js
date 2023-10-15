const getModuleState = state => state.issues;

export const getIssuesSearch = state =>
    getModuleState(state).issuesSearch;