import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';

// inicializar estados
const initialState = {
    issuesSearch: null
}

const issuesSearch = (state = initialState.issuesSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_ALL_ISSUES_COMPLETED:
            return action.issuesSearch;

        case actionTypes.CLEAR_ISSUES:
            return initialState.issuesSearch;

        default:
            return state;
    }
}

const reducer = combineReducers({
    issuesSearch,
});

export default reducer;