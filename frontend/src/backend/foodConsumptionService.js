import {config, appFetch} from "./appFetch";

export const createFoodConsumption = (foodConsumption, onSuccess, onErrors) => {
    appFetch('/foodConsumption/registerFoodConsumption', config('POST', foodConsumption),
        onSuccess, onErrors);
}

export const createSeveralFoodConsumptions = (foodConsumptionList, onSuccess, onErrors) => {
    appFetch('/foodConsumption/registerSeveralFoodConsumptions', config('POST', foodConsumptionList),
        onSuccess, onErrors);
}