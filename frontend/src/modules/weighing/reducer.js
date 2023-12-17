import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';

// inicialiazar estados
const initialState = {
    weighingsSearch: null
}

const weighingsSearch = (state = initialState.weighingsSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_ALL_WEIGHINGS_COMPLETED:
            return action.weighingsSearch;

        case actionTypes.CLEAR_WEIGHINGS:
            return initialState.weighingsSearch;

        default:
            return state;
    }
}

const reducer = combineReducers({
    weighingsSearch
});

export default reducer;