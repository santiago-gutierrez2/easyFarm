package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FoodPurchaseDao extends PagingAndSortingRepository<FoodPurchase, Long>, CustomizedFoodPurchaseDao {
    Slice<FoodPurchase> findByMadeByFarmIdOrderByPurchaseDateDesc(Long farmId, Pageable pageable);

    List<FoodPurchase> findFoodPurchaseByMadeByFarmIdOrderByPurchaseDateDesc(Long farmId);

    @Query("Select fp from FoodPurchase fp " +
            "join User u ON fp.madeBy.id = u.id " +
            "left join FoodConsumption fc ON fp.id = fc.foodBatch.id " +
            "where u.id = :farmId " +
            "group by fp.id " +
            "Having COALESCE(fp.kilos - COALESCE(SUM(fc.kilos), 0), fp.kilos) > 0")
    List<FoodPurchase> getAllFoodPurchasesWithStockLeft(Long farmId);

    @Query("select SUM(fc.kilos) from FoodConsumption fc Where fc.foodBatch.id = :foodPurchaseId")
    Long getAllKilosConsumedByFoodPurchaseId(Long foodPurchaseId);
}
