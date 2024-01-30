package es.udc.paproject.backend.model.entities;

import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.Tuple;
import java.util.List;

public interface FoodConsumptionDao extends PagingAndSortingRepository<FoodConsumption, Long>, CustomizedFoodConsumptionDao {
    Slice<FoodConsumption> findByConsumedByBelongsToIdOrderByDateDesc(Long farmId, Pageable pageable);

    List<FoodConsumption> findFoodConsumptionByFoodBatchId(Long foodBatchId);

    List<FoodConsumption> findFoodConsumptionByFoodBatchIdOrderByDateDesc(Long foodBatchId);

    /*@Query("SELECT SUM(c.kilos) as kilos, FUNCTION('trunc', c.date, 'DAY') as consumptionDate, c.foodBatch.id as foodBatchId " +
            "FROM FoodConsumption c " +
            *//*"JOIN FoodPurchase fp on fp.id = c.foodBatch.id "+*//*
            "Where c.foodBatch.id = :foodBatchId " +
            "GROUP BY FUNCTION('trunc', c.date, 'DAY'), c.foodBatch.id")
    List<Tuple> getConsumptionChartData(Long foodBatchId);*/

    @Query("SELECT NEW es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto(SUM(c.kilos), DATE_FORMAT(c.date, '%Y-%m-%d'), c.foodBatch.id) " +
            "FROM FoodConsumption c " +
            "WHERE c.foodBatch.id = :foodBatchId " +
            "GROUP BY DATE_FORMAT(c.date, '%Y-%m-%d'), c.foodBatch ")
    List<ConsumptionChartDto> getConsumptionChartData(Long foodBatchId);
}
