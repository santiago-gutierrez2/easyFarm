package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedFoodConsumptionDao {
    Slice<FoodConsumption> find(Long farmId, Long animalId, Long foodBatchId, String startDate, String endDate,
                                int page, int size);
}
