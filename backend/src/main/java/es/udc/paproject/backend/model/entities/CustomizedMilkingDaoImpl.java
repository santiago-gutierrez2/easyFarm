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

public class CustomizedMilkingDaoImpl implements CustomizedMilkingDao{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Slice<Milking> findAll(Long farmId, Long animalId, Integer startLiters, Integer endLiters, String startDate, String endDate, int page, int size) {
        String queryString = "SELECT m from Milking m " +
                "left join Animal a on m.animalMilked.id = a.id " +
                "WHERE a.belongsTo.id = " + farmId.toString();

        if (animalId != null || startLiters != null || endLiters != null || startDate != null || endDate != null) {
            queryString += " AND ";
        }

        // animalId
        if (animalId  != null) {
            queryString += "m.animalMilked.id = :animalId ";
        }

        // startLiters
        if (startLiters != null) {
            if (animalId != null) {
                queryString += " AND ";
            }
            queryString += "m.liters >= :startLiters ";
        }

        // endLiters
        if (endLiters != null) {
            if (animalId != null || startLiters != null) {
                queryString += " AND ";
            }
            queryString += "m.liters <= :endLiters ";
        }

        // startDate
        if (startDate != null) {
            if (animalId != null || startLiters != null || endLiters != null) {
                queryString += " AND ";
            }
            queryString += "m.date >= :startDate ";
        }

        // endDate
        if (endDate != null) {
            if (animalId != null || startLiters != null || endLiters != null || startDate != null) {
                queryString += " AND ";
            }
            queryString += "m.date <= :endDate ";
        }

        queryString += " ORDER BY m.date DESC";
        Query query = entityManager.createQuery(queryString).setFirstResult(page*size).setMaxResults(size+1);

        // animalId
        if (animalId  != null) {
            query.setParameter("animalId", animalId);
        }

        // startKilos
        if (startLiters != null) {
            query.setParameter("startLiters", startLiters);
        }

        // endKilos
        if (endLiters != null) {
            query.setParameter("endLiters", endLiters);
        }

        // startDate
        if (startDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime sd = LocalDateTime.parse(startDate + " 00:00:01", formatter);
            sd = sd.plusDays(1);
            query.setParameter("startDate", sd);
        }

        // endDate
        if (endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ed = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            ed = ed.plusDays(1);
            query.setParameter("endDate", ed);
        }

        List<Milking> milkings = query.getResultList();
        boolean hasNext = milkings.size() == (size+1);

        if (hasNext) {
            milkings.remove(milkings.size()-1);
        }

        return new SliceImpl<>(milkings, PageRequest.of(page, size), hasNext);
    }
}
