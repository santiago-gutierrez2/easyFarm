const getModuleState = state => state.animals;

export const getAnimalsSearch = state =>
    getModuleState(state).animalsSearch;