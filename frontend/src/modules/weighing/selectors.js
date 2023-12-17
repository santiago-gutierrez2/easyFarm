const getModuleState = state => state.weighing;

export const getWeighingsSearch = state =>
    getModuleState(state).weighingsSearch;