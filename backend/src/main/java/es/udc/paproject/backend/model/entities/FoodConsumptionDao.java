package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FoodConsumptionDao extends PagingAndSortingRepository<FoodConsumption, Long>, CustomizedFoodConsumptionDao {
    Slice<FoodConsumption> findByConsumedByBelongsToIdOrderByDateDesc(Long farmId, Pageable pageable);

    List<FoodConsumption> findFoodConsumptionByFoodBatchId(Long foodBatchId);
}
