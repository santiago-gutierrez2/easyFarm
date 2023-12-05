package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomizedFoodConsumptionDaoImpl implements CustomizedFoodConsumptionDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<FoodConsumption> find(Long farmId, Long animalId, Long foodBatchId, String startDate, String endDate, int page, int size) {
        String queryString = "SELECT f from FoodConsumption f " +
                "left join Animal a on f.consumedBy.id = a.id " +
                "WHERE a.belongsTo.id = " + farmId.toString();


        if (animalId != null || foodBatchId != null || startDate != null || endDate != null) {
            queryString += " AND ";
        }

        // animalId
        if (animalId != null) {
            queryString += "f.consumedBy.id = :animalId ";
        }

        // foodBatchId
        if (foodBatchId != null) {
            if (animalId != null) {
                queryString += " AND ";
            }
            queryString += "f.foodBatch.id = :foodBatchId ";
        }

        // startDate
        if (startDate != null) {
            if (animalId != null || foodBatchId != null) {
                queryString += " AND ";
            }
            queryString += "f.date > :startDate ";
        }

        // endDate
        if (endDate != null) {
            if (animalId != null || foodBatchId != null || startDate != null) {
                queryString += " AND ";
            }
            queryString += "f.date < :endDate ";
        }

        queryString += " ORDER BY f.date DESC";
        Query query = entityManager.createQuery(queryString).setFirstResult(page*size).setMaxResults(size+1);

        // animalId
        if (animalId != null) {
            query.setParameter("animalId", animalId);
        }

        // foodBatchId
        if (foodBatchId != null) {
            query.setParameter("foodBatchId", foodBatchId);
        }

        // startDate
        if (startDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime sd = LocalDateTime.parse(startDate + " 00:00:01", formatter);
            query.setParameter("startDate", sd);
        }

        // endDate
        if (endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ed = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            query.setParameter("endDate", ed);
        }

        List<FoodConsumption> foodConsumptions = query.getResultList();
        boolean hasNext = foodConsumptions.size() == (size+1);

        if (hasNext) {
            foodConsumptions.remove(foodConsumptions.size()-1);
        }

        return new SliceImpl<>(foodConsumptions, PageRequest.of(page, size), hasNext);

    }
}
