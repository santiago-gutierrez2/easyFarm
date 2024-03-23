package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.services.AnimalService;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.FoodConsumptionService;
import es.udc.paproject.backend.model.services.FoodPurchaseService;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.StockChartDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FoodConsumptionServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private FoodConsumptionService foodConsumptionService;
    @Autowired
    private FoodPurchaseService foodPurchaseService;
    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserDao userDao;

    private FoodPurchase createFoodPurchase(String productName, Integer kilos) {
        User user = userDao.findById(1L).get();
        FoodPurchase fp = new FoodPurchase(productName, "ingredients", "supplier", kilos, BigDecimal.valueOf(10), user);
        foodPurchaseService.createFoodPurchase(fp);
        return fp;
    }

    private Animal createAnimal(String name, Long identificationCode, boolean isMale) {
        User user = userDao.findById(1L).get();
        Animal animal = new Animal(name, identificationCode, LocalDateTime.now(), isMale,
                "TEST", user.getFarm(), false);
        animalService.createAnimal(animal);
        return animal;
    }

    private FoodConsumption createFoodConsumption(FoodPurchase fp, Integer kilos, Animal animal) {
        return new FoodConsumption(kilos, fp, animal);
    }

    @Test
    public void testCreateFoodConsumption() {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        assertNotNull(fc.getId());
    }

    @Test
    public void testCreateSeveralFoodConsumptions() {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        FoodConsumption fc1 = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createSeveralFoodConsumption(List.of(fc, fc1));

        assertNotNull(fc.getId());
        assertNotNull(fc1.getId());
    }

    @Test
    public void testUpdateFoodConsumption() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        // update food consumption
        foodConsumptionService.updateFoodConsumption(fc.getId(), 12);

        assertEquals(12, fc.getKilos());
    }

    @Test
    public void testUpdateFoodConsumptionWithNonExistentFC() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.updateFoodConsumption(NON_EXISTENT_ID, 10));
    }

    @Test
    public void testDeleteFoodConsumption() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        // delete food consumption
        foodConsumptionService.deleteFoodConsumption(fc.getId());

        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.getFoodConsumptionById(fc.getId()));
    }

    @Test
    public void testDeleteNonExistentFoodConsumption() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.deleteFoodConsumption(NON_EXISTENT_ID));
    }

    @Test
    public void testGetFoodConsumptionById() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        FoodConsumption obtainedFC = foodConsumptionService.getFoodConsumptionById(fc.getId());

        assertEquals(fc, obtainedFC);
    }

    @Test
    public void testGetAllFoodConsumptions() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        FoodConsumption fc1 = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);
        foodConsumptionService.createFoodConsumption(fc1);

        Block<FoodConsumption> fcs = foodConsumptionService.getAllFoodConsumptions(1L, null, null,
                null, null, 0, 10);

        assertEquals(2, fcs.getItems().size());
    }

    @Test
    public void testGetAllFoodConsumptionsWithFilter() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 30);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        FoodConsumption fc1 = createFoodConsumption(fp, 20, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);
        foodConsumptionService.createFoodConsumption(fc1);

        Block<FoodConsumption> fcs = foodConsumptionService.getAllFoodConsumptions(1L, List.of(animal.getId()), fp.getId(),
                null, null, 0, 10);

        assertEquals(2, fcs.getItems().size());
    }

    @Test
    public void testGetAllFoodConsumptionsWithNoExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.getAllFoodConsumptions(NON_EXISTENT_ID, null, null,
                        null, null, 0, 10));
    }

    @Test
    public void testGetStockChart() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        List<StockChartDto> stockChartDtos = foodConsumptionService.getStockChart(1L);

        assertEquals(1, stockChartDtos.size());
    }

    @Test
    public void testGetStockChartWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.getStockChart(NON_EXISTENT_ID));
    }

    @Test
    public void testGetConsumptionChartData() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        List<ConsumptionChartDto> consumptionChartDtos = foodConsumptionService.getConsumptionChartData(1L, fp.getId());

        assertEquals(1, consumptionChartDtos.size());
    }

    @Test
    public void testGetConsumptionChartDataWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.getConsumptionChartData(NON_EXISTENT_ID, 1L));
    }

    @Test
    public void testGetGeneralConsumptionChartData() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 20);
        Animal animal = createAnimal("TEST", 1L, false);
        FoodConsumption fc = createFoodConsumption(fp, 10, animal);
        // create FoodConsumption
        foodConsumptionService.createFoodConsumption(fc);

        List<ConsumptionChartDto> consumptionChartDtos = foodConsumptionService.getGeneralConsumptionChartData(1L);

        assertEquals(1, consumptionChartDtos.size());
    }

    @Test
    public void testGetGeneralConsumptionChartDataWithNonExistenUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodConsumptionService.getGeneralConsumptionChartData(NON_EXISTENT_ID));
    }
}
