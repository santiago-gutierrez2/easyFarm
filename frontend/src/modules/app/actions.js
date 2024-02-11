import * as actionTypes from './actionTypes';

export const loading = () => ({
    type: actionTypes.LOADING
});

export const loaded = () => ({
    type: actionTypes.LOADED
});

export const error = error => ({
    type: actionTypes.ERROR,
    error
});

export const activeItem = activeItem => ({
    type: actionTypes.ACTIVE_ITEM_UPDATED,
    activeItem
})