package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.Animal;
import es.udc.paproject.backend.model.entities.Milking;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.AnimalService;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.MilkingService;
import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingChartDto;
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
public class MilkingServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private MilkingService milkingService;
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

    private Milking createMilking(Animal animal, Integer liters) {
        User user = userDao.findById(1L).get();
        return new Milking(liters, user, animal);
    }

    @Test
    public void testCreateMilking() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        // create milking
        milkingService.createMilking(milking);

        assertNotNull(milking.getId());
    }

    @Test
    public void testUpdateMilking() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        // create milking
        milkingService.createMilking(milking);

        // update milking
        milkingService.updateMilking(milking.getId(), 15);

        Milking updatedMilking = milkingService.getMilkingById(milking.getId());

        assertEquals(15, updatedMilking.getLiters());
    }

    @Test
    public void testUpdateNonExistentMilking() {
        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.updateMilking(NON_EXISTENT_ID, 10));
    }

    @Test
    public void testDeleteMilkingRecord() throws InstanceNotFoundException, PermissionException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        // create milking
        milkingService.createMilking(milking);

        // delete milking record
        milkingService.deleteMilkingRecord(1L, milking.getId());

        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.getMilkingById(milking.getId()));
    }

    @Test
    public void testDeleteNonExistingMilkingRecord() {
        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.deleteMilkingRecord(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testDeleteMilkingRecordWithNonExistentUser() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        // create milking
        milkingService.createMilking(milking);

        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.deleteMilkingRecord(NON_EXISTENT_ID, milking.getId()));
    }

    @Test
    public void testDeleteMilkingRecordWithOtherEmployee() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        // create milking
        milkingService.createMilking(milking);

        assertThrows(PermissionException.class,
                () -> milkingService.deleteMilkingRecord(2L, milking.getId()));
    }

    @Test
    public void testGetAllMilkings() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        Milking milking1 = createMilking(animal, 15);
        // create milking
        milkingService.createMilking(milking);
        milkingService.createMilking(milking1);

        // get all milkings
        Block<Milking> milkings = milkingService.getAllMilkings(1L, null, null, null,
                null, null, 0, 10);

        assertEquals(2, milkings.getItems().size());
    }

    @Test
    public void testGetAllMilkingsWithFilter() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        Milking milking1 = createMilking(animal, 15);
        // create milking
        milkingService.createMilking(milking);
        milkingService.createMilking(milking1);

        // get all milkings
        Block<Milking> milkings = milkingService.getAllMilkings(1L, animal.getId(), 11, 15,
                null, null, 0, 10);

        assertEquals(1, milkings.getItems().size());
    }

    @Test
    public void testGetAllMilkingsWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.getAllMilkings(NON_EXISTENT_ID, null, 11, 15,
                        null, null, 0, 10));
    }

    @Test
    public void testFindMilkingByAnimalMilkedId() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        Milking milking1 = createMilking(animal, 15);
        // create milking
        milkingService.createMilking(milking);
        milkingService.createMilking(milking1);

        // find milking chart data
        List<MilkingChartDto> milkingChartDtos = milkingService.findMilkingByAnimalMilkedId(1L, animal.getId());

        // its 1 because milkings where made in the same day
        assertEquals(1, milkingChartDtos.size());
    }

    @Test
    public void testFindMilkingByAnimalMilkedIdWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.findMilkingByAnimalMilkedId(NON_EXISTENT_ID, 1L));
    }

    @Test
    public void testFindMilkingByAnimalMilkedIdWithNonExistentAnimal() {
        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.findMilkingByAnimalMilkedId(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testGeneralMilkProduction() throws InstanceNotFoundException {
        Animal animal = createAnimal("TEST", 1L, false);
        Milking milking = createMilking(animal, 10);
        Milking milking1 = createMilking(animal, 15);
        // create milking
        milkingService.createMilking(milking);
        milkingService.createMilking(milking1);

        // get general milking chart data
        List<MilkingChartDto> milkings = milkingService.getGeneralMilkProduction(1L);

        assertEquals(1, milkings.size());
    }

    @Test
    public void testGeneralMilkProductionWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> milkingService.getGeneralMilkProduction(NON_EXISTENT_ID));
    }
}
