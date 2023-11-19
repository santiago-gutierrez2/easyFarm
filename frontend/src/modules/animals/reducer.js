import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';

// incializar estados
const initialState = {
    animalsSearch: null
}

const animalsSearch = (state = initialState.animalsSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_ALL_ANIMALS_COMPLETED:
            return action.animalsSearch;

        case actionTypes.CLEAR_ANIMALS:
            return initialState.animalsSearch;

        default:
            return state;
    }
}

const reducer = combineReducers({
    animalsSearch,
});

export default reducer;