package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.FoodPurchase;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseDto;

import java.math.BigDecimal;
import java.util.List;

public interface FoodPurchaseService {

    void createFoodPurchase(FoodPurchase foodPurchase);

    FoodPurchase updateFoodPurchase(Long id, String productName, String ingredients, String supplier, Integer kilos,
                                    BigDecimal unitPrice) throws InstanceNotFoundException;

    void deleteFoodPurchase(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    FoodPurchase getFoodPurchaseById(Long userId, Long id) throws InstanceNotFoundException;

    Block<FoodPurchase> getAllFoodPurchases(Long userId, String productName, String supplier, String startDate, String endDate,
        int page, int size) throws InstanceNotFoundException;

    List<FoodPurchase> getListOfAllFoodPurchases(Long userId) throws InstanceNotFoundException;

    List<FoodPurchaseDto> getAllAvailablesFoodBatches(Long userId) throws InstanceNotFoundException;
}
