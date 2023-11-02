package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodPurchaseDao extends PagingAndSortingRepository<FoodPurchase, Long>, CustomizedFoodPurchaseDao {
    Slice<FoodPurchase> findByMadeByFarmIdOrderByPurchaseDateDesc(Long farmId, Pageable pageable);
}
