package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.FarmDao;
import es.udc.paproject.backend.model.entities.FoodPurchase;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.exceptions.IncorrectKilosValueException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.FoodPurchaseService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseWithAvailableKilosDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FoodPurchaseServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private FoodPurchaseService foodPurchaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private FarmDao farmDao;
    @Autowired
    private UserDao userDao;

    private FoodPurchase createFoodPurchase(String productName, Integer kilos) {
        User user = userDao.findById(1L).get();
        return new FoodPurchase(productName, "ingredients", "supplier", kilos, BigDecimal.valueOf(10), user);
    }

    @Test
    public void testCreateFoodPurchase() {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);
        assertNotNull(fp.getId());
    }

    @Test
    public void testUpdateFoodPurchase() throws InstanceNotFoundException, IncorrectKilosValueException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);

        //update foodPurchase
        fp = foodPurchaseService.updateFoodPurchase(fp.getId(), "UPDATED", "UPDATED", "UPDATED",
                20, fp.getUnitPrice());

        assertEquals("UPDATED", fp.getProductName());
    }

    @Test
    public void testUpdateFoodPurchaseWithNonExistingFP() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodPurchaseService.updateFoodPurchase(NON_EXISTENT_ID, "UPDATED", "UPDATED", "UPDATED",
                        20, BigDecimal.valueOf(10)));
    }

    @Test
    public void testDeleteFoodPurchase() throws InstanceNotFoundException, PermissionException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);

        // delete foodPurchase
        foodPurchaseService.deleteFoodPurchase(1L, fp.getId());

        assertThrows(InstanceNotFoundException.class,
                () -> foodPurchaseService.getFoodPurchaseById(1L, fp.getId()));
    }

    @Test
    public void testDeleteFoodPurchaseWithNonExistingUser() {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);

        assertThrows(PermissionException.class,
                () -> foodPurchaseService.deleteFoodPurchase(NON_EXISTENT_ID, fp.getId()));
    }

    @Test
    public void testGetFoodPurchaseById() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);
        Long fpId = fp.getId();

        fp = foodPurchaseService.getFoodPurchaseById(1L, fpId);

        assertEquals(fpId, fp.getId());
    }

    @Test
    public void testGetAllFoodPurchases() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        FoodPurchase fp1 = createFoodPurchase("TEST1", 20);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);
        foodPurchaseService.createFoodPurchase(fp1);

        Block<FoodPurchase> foodPurchaseBlock = foodPurchaseService.getAllFoodPurchases(1L, null, null,
                null, null, 0, 10);

        assertEquals(2, foodPurchaseBlock.getItems().size());
    }

    @Test
    public void testGetAllFoodPurchasesWithFilter() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        FoodPurchase fp1 = createFoodPurchase("TEST1", 20);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);
        foodPurchaseService.createFoodPurchase(fp1);

        Block<FoodPurchase> foodPurchaseBlock = foodPurchaseService.getAllFoodPurchases(1L, "TEST1", null,
                null, null, 0, 10);

        assertEquals(1, foodPurchaseBlock.getItems().size());
    }

    @Test
    public void testGetAllFoodPurchasesWithNonExitingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodPurchaseService.getAllFoodPurchases(NON_EXISTENT_ID, "TEST1", null,
                        null, null, 0, 10));
    }

    @Test
    public void testGetListOfAllFoodPurchases() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        FoodPurchase fp1 = createFoodPurchase("TEST1", 20);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);
        foodPurchaseService.createFoodPurchase(fp1);

        List<FoodPurchase> fps = foodPurchaseService.getListOfAllFoodPurchases(1L);

        assertEquals(2, fps.size());
    }

    @Test
    public void testGetListOfAllFoodPurchasesWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodPurchaseService.getListOfAllFoodPurchases(NON_EXISTENT_ID));
    }

    @Test
    public void testGetAllAvailablesFoodBatches() throws InstanceNotFoundException {
        FoodPurchase fp = createFoodPurchase("TEST", 10);
        FoodPurchase fp1 = createFoodPurchase("TEST1", 20);
        // create foodPurchase
        foodPurchaseService.createFoodPurchase(fp);
        foodPurchaseService.createFoodPurchase(fp1);

        List<FoodPurchaseWithAvailableKilosDto> fpDTOs = foodPurchaseService.getAllAvailablesFoodBatches(1L);

        assertEquals(2, fpDTOs.size());
    }

    @Test
    public void testGetAllAvailablesFoodBatchesWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> foodPurchaseService.getAllAvailablesFoodBatches(NON_EXISTENT_ID));
    }


}
