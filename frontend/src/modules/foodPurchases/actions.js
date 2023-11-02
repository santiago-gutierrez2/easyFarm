import * as actionTypes from './actionTypes';
import backend from "../../backend";

const getAllFoodPurchasesCompleted = foodPurchasesSearch => ({
    type: actionTypes.FIND_ALL_FOOD_PURCHASES_COMPLETED,
    foodPurchasesSearch
});

const clearFoodPurchases = () => ({
   type: actionTypes.CLEAR_FOOD_PURCHASES
});

export const getAllFoodPurchases = (criteria, onSuccess, onErrors) => dispatch => {
    dispatch(clearFoodPurchases());
    backend.foodService.getAllFoodPurchases(criteria,
        result => {
            dispatch(getAllFoodPurchasesCompleted({criteria, result}));
            onSuccess(result);
        }, error => {
            console.log(error);
            onErrors(error);
        })
}

export const previousGetAllFoodPurchases = criteria =>
    getAllFoodPurchases({...criteria, page: criteria.page - 1}, () => {

    }, () => {

    });


export const nextGetAllFoodPurchases = criteria =>
    getAllFoodPurchases({...criteria, page: criteria.page + 1}, () => {

    }, () => {

    });
