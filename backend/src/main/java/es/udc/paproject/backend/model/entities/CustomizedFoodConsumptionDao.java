package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomizedFoodConsumptionDao {
    Slice<FoodConsumption> find(Long farmId, List<Long> animalId, Long foodBatchId, String startDate, String endDate,
                                int page, int size);
}
