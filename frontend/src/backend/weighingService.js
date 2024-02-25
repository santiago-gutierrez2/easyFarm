import {config, appFetch} from "./appFetch";

export const registerWeighing = (weighing, onSuccess, onErrors) => {
    appFetch('/weighing/registerWeighing', config('POST', weighing), onSuccess, onErrors);
}

export const updateWeighing = (weighingId, weighing, onSuccess, onErrors) => {
    appFetch(`/weighing/${weighingId}`, config('PUT', weighing), onSuccess, onErrors);
}

export const deleteWeighing = (weighingId, onSuccess, onErrors) => {
    appFetch(`/weighing/${weighingId}`, config('DELETE'), onSuccess, onErrors);
}

export const getWeighingById = (weighingId, onSuccess, onErrors) => {
    appFetch(`/weighing/${weighingId}`, config('GET'), onSuccess, onErrors);
}

export const getAllWeighing = ({page, animalId, startKilos, endKilos, startDate, endDate}, onSuccess, onErrors) => {
    let path = `/weighing/AllWeighingByFarmId?page=${page}&size=5`;

    path += animalId != null && animalId > 0 ? `&animalId=${encodeURIComponent(animalId)}` : "";
    path += startKilos != null && startKilos > 0 ? `&startKilos=${encodeURIComponent(startKilos)}` : "";
    path += endKilos != null && endKilos > 0 ? `&endKilos=${encodeURIComponent(endKilos)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";

    appFetch(path, config('GET'), onSuccess, onErrors);
}

export const getAllWeighingsByAnimalId = (animalId, onSuccess, onErrors) => {
    appFetch(`/weighing/allWeighingByAnimalId/${animalId}`, config('GET'), onSuccess, onErrors);
}

export const getMeatProductionEvolution = (onSuccess, onErrors) => {
    appFetch('/weighing/meatProductionEvolution', config('GET'), onSuccess, onErrors);
}