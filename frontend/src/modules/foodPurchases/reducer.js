import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';

// inicializar estados
const initialState = {
    foodPurchasesSearch: null
}

const foodPurchasesSearch = (state = initialState.foodPurchasesSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_ALL_FOOD_PURCHASES_COMPLETED:
            return action.foodPurchasesSearch;

        case actionTypes.CLEAR_FOOD_PURCHASES:
            return initialState.foodPurchasesSearch;

        default:
            return state;
    }
}

const reducer = combineReducers({
    foodPurchasesSearch,
});

export default reducer;