import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';

// inicializar estados
const initialState = {
    foodConsumptionsSearch: null
}

const foodConsumptionsSearch = (state = initialState.foodConsumptionsSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_ALL_FOOD_CONSUMPTIONS_COMPLETED:
            return action.foodConsumptionsSearch;

        case actionTypes.CLEAR_FOOD_CONSUMPTIONS:
            return initialState.foodConsumptionsSearch;

        default:
            return state;
    }
}

const reducer = combineReducers({
    foodConsumptionsSearch,
});

export default reducer;