import * as actionTypes from './actionTypes';
import backend from "../../backend";

const getAllAnimalsCompleted = animalsSearch => ({
    type: actionTypes.FIND_ALL_ANIMALS_COMPLETED,
    animalsSearch
});

const clearAnimals = () => ({
    type: actionTypes.CLEAR_ANIMALS
});

export const getAllAnimals = (criteria, onSuccess, onErrors) => dispatch => {
    dispatch(clearAnimals());
    backend.animalService.getAllAnimals(criteria,
        result => {
            dispatch(getAllAnimalsCompleted({criteria, result}));
            onSuccess(result);
        }, error => {
            console.log(error);
            onErrors(error);
        }
    );
}

export const previousGetAllAnimals = criteria =>
    getAllAnimals({...criteria, page: criteria.page - 1}, ()=>{}, ()=>{});

export const nextGetAllAnimals = criteria =>
    getAllAnimals({...criteria, page: criteria.page + 1}, ()=>{}, ()=>{});