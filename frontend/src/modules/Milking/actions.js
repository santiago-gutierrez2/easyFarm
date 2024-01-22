import * as actionTypes from './actionTypes';
import backend from "../../backend";

const getAllMilkingsCompleted = milkingsSearch => ({
    type: actionTypes.FIND_ALL_MILKINGS_COMPLETED,
    milkingsSearch
});

const clearMilkings = () => ({
    type: actionTypes.CLEAR_MILKINGS
});

export const getAllMilkings = (criteria, onSuccess, onErrors) => dispatch => {
    dispatch(clearMilkings());
    backend.milkingService.getAllMilkings(criteria,
        result => {
            dispatch(getAllMilkingsCompleted({criteria, result}));
            onSuccess(result);
        }, errors => {
            console.log(errors);
            onErrors(errors);
        }
    );
}

export const previousGetAllMilkings = criteria =>
    getAllMilkings({...criteria, page: criteria.page - 1}, () => {

    }, () => {

    });

export const nextGetAllMilkings = criteria =>
    getAllMilkings({...criteria, page: criteria.page + 1}, () => {

    }, () => {

    });

