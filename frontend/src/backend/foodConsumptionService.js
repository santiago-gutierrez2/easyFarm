import {config, appFetch} from "./appFetch";

export const createFoodConsumption = (foodConsumption, onSuccess, onErrors) => {
    appFetch('/foodConsumption/registerFoodConsumption', config('POST', foodConsumption),
        onSuccess, onErrors);
}

export const createSeveralFoodConsumptions = (foodConsumptionList, onSuccess, onErrors) => {
    appFetch('/foodConsumption/registerSeveralFoodConsumptions', config('POST', foodConsumptionList),
        onSuccess, onErrors);
}

export const getAllFoodConsumptions = ({page, animals, foodBatch, startDate, endDate}, onSuccess, onErrors) => {
    let path = `/foodConsumption/allFoodConsumptions?page=${page}&size=5`;

    //path += animal != null && animal.length > 0 ? `&animalsId=${encodeURIComponent(animals)}` : "";
    if (animals != null && animals.length > 0) {
        path += '&animalsId=';
        for (let a of animals) {
            path += `${encodeURIComponent(a)},`
        }
        path = path.slice(0,-1); // get rid of the las comma
    }
    path += foodBatch != null && foodBatch.length > 0 && foodBatch !== '0' ? `&foodBatchId=${encodeURIComponent(foodBatch)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";

    appFetch(path, config('GET'), onSuccess, onErrors);
}

export const getStockChart = (onSuccess, onErrors) => {
    appFetch('/foodConsumption/stockChart', config('GET'), onSuccess, onErrors);
}

export const getFoodConsumptionChartData = (foodBatchId, onSuccess, onErrors) => {
    appFetch(`/foodConsumption/consumptionChart/${foodBatchId}`, config('GET'), onSuccess, onErrors);
}