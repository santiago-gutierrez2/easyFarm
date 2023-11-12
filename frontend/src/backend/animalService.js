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