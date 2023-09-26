package es.udc.paproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodPurchaseDao extends PagingAndSortingRepository<FoodPurchase, Long> {
}
