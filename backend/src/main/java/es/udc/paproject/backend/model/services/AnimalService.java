package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Animal;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.hibernate.tuple.AbstractNonIdentifierAttribute;

public interface AnimalService {

    void createAnimal(Animal animal);

    void updateAnimal(Long id, Animal animal) throws InstanceNotFoundException;

    void setAnimalDead(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    Animal getAnimalById(Long userId, Long id) throws InstanceNotFoundException;

    Block<Animal> getAllAnimals(Long userId, String name, Long identificationNumber, String startDate, String endDate,
        Boolean isMale, int page, int size) throws InstanceNotFoundException;
}
