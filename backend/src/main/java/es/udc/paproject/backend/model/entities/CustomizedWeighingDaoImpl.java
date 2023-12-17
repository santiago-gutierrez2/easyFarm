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

public class CustomizedWeighingDaoImpl implements CustomizedWeighingDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<Weighing> find(Long farmId, Long animalId, Integer startKilos, Integer endKilos, String startDate, String endDate, int page, int size) {
        String queryString = "SELECT w from Weighing w " +
                "left join Animal a on w.animalWeighed.id = a.id " +
                "WHERE a.belongsTo.id = " + farmId.toString();

        if (animalId != null || startKilos != null || endKilos != null || startDate != null || endDate != null) {
            queryString += " AND ";
        }

        // animalId
        if (animalId  != null) {
            queryString += "w.animalWeighed.id = :animalId ";
        }

        // startKilos
        if (startKilos != null) {
            if (animalId != null) {
                queryString += " AND ";
            }
            queryString += "w.kilos > :startKilos ";
        }

        // endKilos
        if (endKilos != null) {
            if (animalId != null || startKilos != null) {
                queryString += " AND ";
            }
            queryString += "w.kilos < :endKilos ";
        }

        // startDate
        if (startDate != null) {
            if (animalId != null || startKilos != null || endKilos != null) {
                queryString += " AND ";
            }
            queryString += "w.date > :startDate ";
        }

        // endDate
        if (endDate != null) {
            if (animalId != null || startKilos != null || endKilos != null || startDate != null) {
                queryString += " AND ";
            }
            queryString += "w.date < :endDate ";
        }

        queryString += " ORDER BY w.date DESC";
        Query query = entityManager.createQuery(queryString).setFirstResult(page*size).setMaxResults(size+1);

        // animalId
        if (animalId  != null) {
            query.setParameter("animalId", animalId);
        }

        // startKilos
        if (startKilos != null) {
            query.setParameter("startKilos", startKilos);
        }

        // endKilos
        if (endKilos != null) {
            query.setParameter("endKilos", endKilos);
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

        List<Weighing> weighings = query.getResultList();
        boolean hasNext = weighings.size() == (size+1);

        if (hasNext) {
            weighings.remove(weighings.size()-1);
        }

        return new SliceImpl<>(weighings, PageRequest.of(page, size), hasNext);
    }
}
