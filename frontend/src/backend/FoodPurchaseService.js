import {config, appFetch} from './appFetch';

export const createFoodPurchase = (foodPurchase, onSuccess, onErrors) => {
    appFetch('/foodPurchase/createFoodPurchase', config('POST', foodPurchase),
        onSuccess, onErrors);
}

export const updateFoodPurchase = (foodPurchaseId, foodPurchase, onSuccess, onErrors) => {
    appFetch(`/foodPurchase/${foodPurchaseId}`, config('PUT', foodPurchase),
        onSuccess, onErrors);
}

export const getFoodPurchaseById = (foodPurchaseId, onSuccess, onErrors) => {
    appFetch(`/foodPurchase/${foodPurchaseId}`, config('GET'), onSuccess, onErrors);
}

export const deleteFoodPurchase = (foodPurchaseId, onSuccess, onErrors) => {
    appFetch(`/foodPurchase/${foodPurchaseId}/deleteFoodPurchase`, config('DELETE'), onSuccess, onErrors);
}

export const getAllFoodPurchases = ({page, productName, supplier, startDate, endDate}, onSuccess, onErrors) => {
    let path = `/foodPurchase/allFoodPurchases?page=${page}&size=5`;

    path += productName?.length > 0 ? `&productName=${encodeURIComponent(productName)}` : "";
    path += supplier?.length > 0 ? `&supplier=${encodeURIComponent(supplier)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";

    appFetch(path, config('GET'), onSuccess, onErrors);
}

export const getListOfAllFoodPurchases = (onSuccess, onErrors) => {
    appFetch('/foodPurchase/getListOfFoodPurchases', config('GET'), onSuccess, onErrors);
}

export const getAllAvailableFoodBatches = (onSuccess, onErrors) => {
    appFetch('/foodPurchase/getAvailableFoodBatches', config('GET'), onSuccess, onErrors);
}