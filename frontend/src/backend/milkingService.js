import {config, appFetch} from "./appFetch";

export const registerMilking = (milking, onSuccess, onErrors) => {
    appFetch('/milking/registerMilking', config('POST', milking), onSuccess, onErrors);
}

export const updateMilking = (milkingId, milking, onSuccess, onErrors) => {
    appFetch(`/milking/${milkingId}`, config('PUT', milking), onSuccess, onErrors);
}

export const deleteMilking = (milkingId, onSuccess, onErrors) => {
    appFetch(`/milking/${milkingId}`, config('DELETE'), onSuccess, onErrors);
}

export const getMilkingById = (milkingId, onSuccess, onErrors) => {
    appFetch(`/milking/${milkingId}`, config('GET'), onSuccess, onErrors);
}

export const getAllMilkings = ({page, animalId, startLiters, endLiters, startDate, endDate}, onSuccess, onErrrors) => {
    let path = `/milking/AllMilkingByFarmId?page=${page}&size=5`;

    path += animalId != null && animalId > 0 ? `&animalId=${encodeURIComponent(animalId)}` : "";
    path += startLiters != null && startLiters > 0 ? `&startLiters=${encodeURIComponent(startLiters)}`: "";
    path += endLiters != null && endLiters > 0 ? `&endLiters=${encodeURIComponent(endLiters)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";

    appFetch(path, config('GET'), onSuccess, onErrrors);
}