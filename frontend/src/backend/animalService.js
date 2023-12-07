import {config, appFetch} from "./appFetch";

export const createAnimal = (animal, onSuccess, onErrors) => {
    appFetch('/animal/registerAnimal', config('POST', animal),
        onSuccess, onErrors);
}

export const updateAnimal = (animalId, animal, onSucces, onErrors) => {
    appFetch(`/animal/${animalId}`, config('PUT', animal),
        onSucces, onErrors);
}

export const getAnimalById = (animalId, onSuccess, onErrors) => {
    appFetch(`/animal/${animalId}`, config('GET'),
        onSuccess, onErrors);
}

export const getAllAnimals = ({page, name, identificationNumber, startDate, endDate, isMale}, onSuccess, onErrors) => {
    let path = `/animal/allAnimals?page=${page}&size=5`;

    path += name?.length > 0 ? `&name=${encodeURIComponent(name)}` : "";
    path += identificationNumber ? `&identificationNumber=${encodeURIComponent(identificationNumber)}` : "";
    path += startDate ? `&startDate=${encodeURIComponent(startDate.toISOString().substring(0,10))}` : "";
    path += endDate ? `&endDate=${encodeURIComponent(endDate.toISOString().substring(0,10))}` : "";
    path += isMale ? `&isMale=${encodeURIComponent(isMale)}` : "";

    appFetch(path, config('GET'), onSuccess, onErrors);
}

export const getAllAnimalsWithLabel = (onSuccess, onErrors) => {
    appFetch('/animal/animalsWithLabel', config('GET'), onSuccess, onErrors);
}