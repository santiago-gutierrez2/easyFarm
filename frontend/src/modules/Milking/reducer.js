import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';

// inicializar estados
const initialState = {
    milkingsSearch: null
}

const milkingsSearch = (state = initialState.milkingsSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_ALL_MILKINGS_COMPLETED:
            return action.milkingsSearch;

        case actionTypes.CLEAR_MILKINGS:
            return initialState.milkingsSearch;

        default:
            return state;
    }
}

const reducer = combineReducers({
    milkingsSearch
});

export default reducer;