package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.Animal;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.entities.Weighing;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.AnimalService;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.WeighingService;
import es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingChartDto;
import es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WeighingServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private WeighingService weighingService;
    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserDao userDao;

    private Animal createAnimal(String name, Long identificationCode, boolean isMale) {
        User user = userDao.findById(1L).get();
        Animal animal = new Animal(name, identificationCode, LocalDateTime.now(), isMale,
                "TEST", user.getFarm(), false);
        animalService.createAnimal(animal);
        return animal;
    }

    private Weighing createWeighing(Animal animal, Integer kilos, Boolean isProduction) {
        User user = userDao.findById(1L).get();
        return new Weighing(kilos, user, animal, isProduction);
    }

    @Test
    public void testCreatWeighing() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        // create weighing
        weighingService.createWeighing(weighing);

        assertNotNull(weighing.getId());
    }

    @Test
    public void testUpdateWeighing() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        // create weighing
        weighingService.createWeighing(weighing);

        // update weighing
        weighingService.updateWeighing(weighing.getId(), 260, false);

        Weighing updatedWeighing = weighingService.getWeighingById(weighing.getId());

        assertEquals(260, updatedWeighing.getKilos());
    }

    @Test
    public void testUpdateNonExistentWeighing() {
        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.updateWeighing(NON_EXISTENT_ID, 200, false));
    }

    @Test
    public void testDeleteWeighing() throws InstanceNotFoundException, PermissionException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        // create weighing
        weighingService.createWeighing(weighing);

        // delete weighing
        weighingService.deleteWeighing(1L, weighing.getId());

        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.getAllWeighingByAnimalId(1L, weighing.getId()));
    }

    @Test
    public void testDeleteNonExistentWeighing() {
        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.deleteWeighing(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testDeleteWeighingWithNonExistentUser() throws InstanceNotFoundException{
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        // create weighing
        weighingService.createWeighing(weighing);

        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.deleteWeighing(NON_EXISTENT_ID, weighing.getId()));
    }

    @Test
    public void testDeleteWeighingWithAnotherUser() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        // create weighing
        weighingService.createWeighing(weighing);

        assertThrows(PermissionException.class,
                () -> weighingService.deleteWeighing(2L, weighing.getId()));
    }

    @Test
    public void testGetAllWeighings() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        Weighing weighing1 = createWeighing(animal, 260, false);
        // create weighing
        weighingService.createWeighing(weighing);
        weighingService.createWeighing(weighing1);

        // get all weighings
        Block<Weighing> weighingBlock = weighingService.getAllWeighings(1L, null, null,
                null, null, null, null, 0, 10);

        assertEquals(2, weighingBlock.getItems().size());
    }

    @Test
    public void testGetAllWeighingWithFilter() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        Weighing weighing1 = createWeighing(animal, 260, false);
        // create weighing
        weighingService.createWeighing(weighing);
        weighingService.createWeighing(weighing1);

        // get all weighings
        Block<Weighing> weighingBlock = weighingService.getAllWeighings(1L, animal.getId(), 251,
                260, null, null, false, 0, 10);

        assertEquals(1, weighingBlock.getItems().size());
    }

    @Test
    public void testGetAllWeighingsWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.getAllWeighings(NON_EXISTENT_ID, null, null,
                        null, null, null, null, 0, 10));
    }

    @Test
    public void testGetAllWeighingsByAnimalId() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, false);
        // create weighing
        weighingService.createWeighing(weighing);

        // get weighing chart data
        List<WeighingDto> weighingDtos = weighingService.getAllWeighingByAnimalId(1L, animal.getId());

        assertEquals(1, weighingDtos.size());
    }

    @Test
    public void testGetAllWeighingsByAnimalWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.getAllWeighingByAnimalId(NON_EXISTENT_ID, 1L));
    }

    @Test
    public void testGetAllWeighingsByAnimalWithNonExistentAnimal() {
        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.getAllWeighingByAnimalId(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testGetFarmProductionEvolution() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, true);
        Weighing weighing = createWeighing(animal, 250, true);
        // create weighing
        weighingService.createWeighing(weighing);

        // get general production
        List<WeighingChartDto> weighingChartDtos = weighingService.getFarmMeatProductionEvolution(1L);

        assertEquals(1, weighingChartDtos.size());
    }

    @Test
    public void testGetFarmProductionEvolutionWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> weighingService.getFarmMeatProductionEvolution(NON_EXISTENT_ID));
    }
}
