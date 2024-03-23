package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.AnimalService;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AnimalServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserService userService;
    @Autowired
    private FarmDao farmDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AnimalDao animalDao;

    private User createEmployee(String userName) throws DuplicateInstanceException {
        Farm farm = farmDao.findById(1L).get();
        User user = new User(userName,"55043207K", "17000058787", "password", "firstName",
                "lastName", userName + "@" + userName + ".com", farm);
        userService.signUp(user);
        return user;
    }

    private Animal createAnimal(String name, Long identificationCode, boolean isMale, Farm farm) {
        return new Animal(name, identificationCode, LocalDateTime.now(), isMale,
                "TEST", farm, false);
    }

    @Test
    public void testCreateAnimal() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);

        // create animal
        animalService.createAnimal(animal);

        // get created Animal
        Animal animalCreated = animalService.getAnimalById(1L, animal.getId());

        assertEquals(animal, animalCreated);
    }

    @Test
    public void testUpdateAnimal() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);

        // create animal
        animalService.createAnimal(animal);

        //create new Animal object with updates
        Animal updateAnimal = createAnimal("updated", 2L, true, farm);
        updateAnimal = animalService.updateAnimal(animal.getId(), updateAnimal);

        assertEquals(animal.getId(), updateAnimal.getId());
        assertEquals(animal.getName(), updateAnimal.getName());
    }

    @Test
    public void testUpdateNonExistingAnimal() {
        Farm farm = farmDao.findById(1L).get();
        Animal updateAnimal = createAnimal("updated", 2L, false, farm);
        assertThrows(InstanceNotFoundException.class,
                () -> animalService.updateAnimal(NON_EXISTENT_ID, updateAnimal));
    }

    @Test
    public void testSetAnimalDead() throws InstanceNotFoundException, PermissionException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);

        // create animal
        animalService.createAnimal(animal);

        // set animal as dead
        animalService.setAnimalDead(1L, animal.getId());

        assertTrue(animal.getIsDead());
    }

    @Test
    public void testSetAnimalDeadWithNonExistingAnimal() {
        assertThrows(InstanceNotFoundException.class,
                () -> animalService.setAnimalDead(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testSetAnimalDeadWithNonExistingUser() {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);

        // create animal
        animalService.createAnimal(animal);

        assertThrows(PermissionException.class,
                () -> animalService.setAnimalDead(NON_EXISTENT_ID, animal.getId()));
    }

    @Test
    public void testGetAnimalByIdWithNonExistingAnimal() {
        assertThrows(InstanceNotFoundException.class,
                () -> animalService.getAnimalById(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testGetAllAnimals() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);
        Animal animal1 = createAnimal("test1", 2L, false, farm);
        // create animals
        animalService.createAnimal(animal);
        animalService.createAnimal(animal1);

        Block<Animal> animals = animalService.getAllAnimals(1L, null, null, null,
                null , null, null, 0, 10);

        assertEquals(2, animals.getItems().size());
    }

    @Test
    public void testGetAllAnimalsWithFilter() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);
        Animal animal1 = createAnimal("test1", 2L, false, farm);
        // create animals
        animalService.createAnimal(animal);
        animalService.createAnimal(animal1);

        Block<Animal> animals = animalService.getAllAnimals(1L, "test1", 2L, null,
                null , false, false, 0, 10);

        assertEquals(1, animals.getItems().size());
    }

    @Test
    public void testGetAllAnimalsWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> animalService.getAllAnimals(NON_EXISTENT_ID, null, null, null,
                        null , null, null, 0, 10));
    }

    @Test
    public void testGetAnimalWithLabel() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);
        Animal animal1 = createAnimal("test1", 2L, true, farm);
        // create animals
        animalService.createAnimal(animal);
        animalService.createAnimal(animal1);

        // get All animals
        List<Animal> animals = animalService.getAnimalsWithLabel(1L, true, false);

        assertEquals(2, animals.size());
    }

    @Test
    public void testGetAnimalWithLabelOnlyFemales() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);
        Animal animal1 = createAnimal("test1", 2L, true, farm);
        // create animals
        animalService.createAnimal(animal);
        animalService.createAnimal(animal1);

        // get All animals
        List<Animal> animals = animalService.getAnimalsWithLabel(1L, true, true);

        assertEquals(1, animals.size());
    }

    @Test
    public void testGetAnimalsWithLabelOnlyAlive() throws InstanceNotFoundException, PermissionException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);
        Animal animal1 = createAnimal("test1", 2L, true, farm);
        // create animals
        animalService.createAnimal(animal);
        animalService.createAnimal(animal1);
        // set animal1 as dead
        animalService.setAnimalDead(1L, animal1.getId());

        // get All animals
        List<Animal> animals = animalService.getAnimalsWithLabel(1L, false, false);

        assertEquals(1, animals.size());
    }

    @Test
    public void testGetAnimalsWithLabelWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> animalService.getAnimalsWithLabel(NON_EXISTENT_ID, true, false));
    }

    @Test
    public void testGetAnimalFoodConsumptionChart() throws InstanceNotFoundException {
        Farm farm = farmDao.findById(1L).get();
        Animal animal = createAnimal("test", 1L, false, farm);

        // create animal
        animalService.createAnimal(animal);

        List<ConsumptionChartDto> consumptionChartDtos = animalService.getAnimalFoodConsumptionChart(1L, animal.getId());

        assertEquals(0, consumptionChartDtos.size());
    }

    @Test
    public void testGetAnimalFoodConsumptionChartWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> animalService.getAnimalFoodConsumptionChart(NON_EXISTENT_ID, 1L));
    }
}
