package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    @Autowired
    private AnimalDao animalDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void createAnimal(Animal animal) {
        animalDao.save(animal);
    }

    @Override
    public void updateAnimal(Long id, Animal newAnimal) throws InstanceNotFoundException {
        Optional<Animal> optionalAnimal = animalDao.findById(id);

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.animal", id);
        }

        Animal animal = optionalAnimal.get();
        animal.setName(newAnimal.getName());
        animal.setIdentificationNumber(newAnimal.getIdentificationNumber());
        animal.setBirthDate(newAnimal.getBirthDate());
        animal.setPhysicalDescription(newAnimal.getPhysicalDescription());
        animal.setIsMale(newAnimal.getIsMale());
        animal.setIsDead(newAnimal.getIsDead());

        animalDao.save(animal);

    }

    @Override
    public void setAnimalDead(Long userId, Long id) throws InstanceNotFoundException, PermissionException {
        Optional<Animal> optionalAnimal = animalDao.findById(id);
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.animal", id);
        } else if (optionalUser.isEmpty() || optionalUser.get().getRole() != User.RoleType.ADMIN) {
            throw new PermissionException();
        }

        Animal animal = optionalAnimal.get();
        animal.setIsDead(true);

        animalDao.save(animal);
    }

    @Override
    public Animal getAnimalById(Long userId, Long id) throws InstanceNotFoundException {
        Optional<Animal> optionalAnimal = animalDao.findById(id);

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.animal", id);
        }

        return optionalAnimal.get();
    }

    @Override
    public Block<Animal> getAllAnimals(Long userId, String name, Long identificationNumber, String startDate, String endDate,
       Boolean isMale, int page, int size) throws InstanceNotFoundException {

        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        Slice<Animal> animalSlice;
        if (name != null || identificationNumber != null || startDate != null || endDate != null || isMale != null) {
            animalSlice = null;
        } else {
            animalSlice = animalDao.findByBelongsToIdOrderByBirthDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        List<Animal> animalList = animalSlice.getContent();

        return new Block<>(animalList, animalSlice.hasNext());
    }
}
