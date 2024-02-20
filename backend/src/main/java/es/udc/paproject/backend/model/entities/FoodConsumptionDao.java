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

    @Query("SELECT NEW es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto(SUM(c.kilos), DATE_FORMAT(c.date, '%Y-%m-%d'), c.foodBatch.id) " +
            "FROM FoodConsumption c " +
            "WHERE c.foodBatch.id = :foodBatchId " +
            "GROUP BY DATE_FORMAT(c.date, '%Y-%m-%d'), c.foodBatch ")
    List<ConsumptionChartDto> getConsumptionChartData(Long foodBatchId);

    @Query("SELECT NEW es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto(SUM(c.kilos), DATE_FORMAT(c.date, '%Y-%m-%d'), c.foodBatch.id) " +
            "FROM FoodConsumption c " +
            "WHERE c.consumedBy.id = :animalId " +
            "Group By DATE_FORMAT(c.date, '%Y-%m-%d'), c.foodBatch ")
    List<ConsumptionChartDto> getAnimalConsumptionChartData(Long animalId);

    @Query("SELECT NEW es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto(SUM(c.kilos), DATE_FORMAT(c.date, '%Y-%m-%d'), f.id) " +
            "FROM FoodConsumption c " +
            "Left join Animal a on a.id = c.consumedBy.id " +
            "Left join Farm f on f.id = a.belongsTo.id " +
            "Where f.id = :farmId " +
            "GROUP BY DATE_FORMAT(c.date, '%Y-%m-%d'), f.id ")
    List<ConsumptionChartDto> getGeneralConsumptionChart(long farmId);
}
