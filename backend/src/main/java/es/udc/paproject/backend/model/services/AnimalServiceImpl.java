package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    @Autowired
    private AnimalDao animalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FoodConsumptionDao foodConsumptionDao;
    @Autowired
    private MilkingDao milkingDao;

    @Override
    public void createAnimal(Animal animal) {
        animalDao.save(animal);
    }

    @Override
    public Animal updateAnimal(Long id, Animal newAnimal) throws InstanceNotFoundException {
        Optional<Animal> optionalAnimal = animalDao.findById(id);

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.animal", id);
        }

        Animal animal = optionalAnimal.get();
        animal.setName(newAnimal.getName());
        animal.setIdentificationNumber(newAnimal.getIdentificationNumber());
        animal.setBirthDate(newAnimal.getBirthDate());
        animal.setPhysicalDescription(newAnimal.getPhysicalDescription());
        if (newAnimal.getIsMale() != null) {
            animal.setIsMale(newAnimal.getIsMale());
            if (!newAnimal.getIsMale()) {
                milkingDao.deleteAllByAnimalMilkedId(animal.getId());
            }
        }
        animal.setIsDead(newAnimal.getIsDead());

        animal = animalDao.save(animal);
        return animal;

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
            animalSlice = animalDao.find(farm.getId(), name, identificationNumber, startDate, endDate, isMale, page, size);
        } else {
            animalSlice = animalDao.findByBelongsToIdOrderByBirthDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        List<Animal> animalList = animalSlice.getContent();

        return new Block<>(animalList, animalSlice.hasNext());
    }

    @Override
    public List<Animal> getAnimalsWithLabel(Long userId, boolean all, boolean onlyFemale) throws InstanceNotFoundException {

        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        List<Animal> animalsList = animalDao.findAnimalsByBelongsToIdOrderByBirthDateDesc(farm.getId());

        if (onlyFemale) {
            animalsList = animalsList.stream().filter(a -> !a.getIsMale()).collect(Collectors.toList());
        }

        if (all) {
            return animalsList;
        }

        return animalsList.stream().filter(a -> !a.getIsDead()).collect(Collectors.toList());

    }

    @Override
    public List<ConsumptionChartDto> getAnimalFoodConsumptionChart(Long userId, Long animalId) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        return foodConsumptionDao.getAnimalConsumptionChartData(animalId);
    }
}
