const getModuleState = state => state.milking;

export const getMilkingSearch = state =>
    getModuleState(state).milkingsSearch;