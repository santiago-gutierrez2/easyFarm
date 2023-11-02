package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.FoodPurchase;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;

import java.math.BigDecimal;

public interface FoodPurchaseService {

    void createFoodPurchase(FoodPurchase foodPurchase);

    FoodPurchase updateFoodPurchase(Long id, String productName, String ingredients, String supplier, Integer kilos,
                                    BigDecimal unitPrice) throws InstanceNotFoundException;

    void deleteFoodPurchase(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    FoodPurchase getFoodPurchaseById(Long userId, Long id) throws InstanceNotFoundException;

    Block<FoodPurchase> getAllFoodPurchases(Long userId, String productName, String supplier, String startDate, String endDate,
        int page, int size) throws InstanceNotFoundException;
}
