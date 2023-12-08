import * as actionTypes from './actionTypes';
import backend from "../../backend";
import {getAllFoodConsumptions} from "../../backend/foodConsumptionService";

const getAllFoodConsumptionsCompleted = foodConsumptionsSearch => ({
    type: actionTypes.FIND_ALL_FOOD_CONSUMPTIONS_COMPLETED,
    foodConsumptionsSearch
});

const clearFoodConsumptions = () => ({
    type: actionTypes.CLEAR_FOOD_CONSUMPTIONS
});

export const getALLFoodConsumptions = (criteria, onSuccess, onErrors) => dispatch => {
    dispatch(clearFoodConsumptions());
    backend.foodConsumptionService.getAllFoodConsumptions(criteria,
        result => {
            dispatch(getAllFoodConsumptionsCompleted({criteria, result}));
            onSuccess(result);
        }, error => {
            console.log(error);
            onErrors(error);
        }
    );
}

export const previousGetAllFoodConsumptions = criteria =>
    getALLFoodConsumptions({...criteria, page: criteria.page - 1}, () => {

    }, () => {

    });

export const nextGetAllFoodConsumptions = criteria =>
    getALLFoodConsumptions({...criteria, page: criteria.page - 1}, () => {

    }, () => {

    });