package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedFoodPurchaseDao {
    Slice<FoodPurchase> find(Long farmId, String productName, String startDate, String endDate,
                      String supplier, int page, int size);
}
