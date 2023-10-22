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

export const getAllFoodPurchases = ({page, productName, startDate, endDate, madeBy}, onSuccess, onErrors) => {
    let path = `/foodPurchase/allFoodPurchases?page=${page}&size=5`;

    path += productName?.length > 0 ? `&productName=${encodeURIComponent(productName)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";
    path += madeBy > 0 ? `&madeBy=${madeBy}` : "";

    appFetch(path, config('GET'), onSuccess, onErrors);
}