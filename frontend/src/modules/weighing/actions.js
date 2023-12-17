import * as actionTypes from './actionTypes';
import backend from "../../backend";

const getAllWeighingsCompleted = weighingsSearch => ({
    type: actionTypes.FIND_ALL_WEIGHINGS_COMPLETED,
    weighingsSearch
});

const clearWeighings = () => ({
    type: actionTypes.CLEAR_WEIGHINGS
});

export const getAllWeighings = (criteria, onSuccess, onErrors) => dispatch => {
    dispatch(clearWeighings());
    backend.weighingService.getAllWeighing(criteria,
        result => {
            dispatch(getAllWeighingsCompleted({criteria, result}));
            onSuccess(result);
        }, error => {
            console.log(error);
            onErrors(error);
        }
    );
}

export const previousGetAllWeighings = criteria =>
    getAllWeighings({...criteria, page: criteria.page - 1}, () => {

    }, () => {

    });

export const nextGetAllWeighings = criteria =>
    getAllWeighings({criteria, page: criteria.page + 1}, () => {

    }, () => {

    });