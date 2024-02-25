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

public class CustomizedAnimalDaoImpl implements CustomizedAnimalDao{

    @PersistenceContext
    private EntityManager entityManager;

    private String[] getTokens(String issueName) {
        if (issueName == null || issueName.isEmpty()) {
            return new String[0];
        } else {
            return issueName.split("\\s");
        }
    }

    @Override
    public Slice<Animal> find(Long farmId, String name, Long identificationNumber, String startDate, String endDate, Boolean isMale, Boolean dead, int page, int size) {

        String[] tokensName = getTokens(name);
        String queryString = "SELECT a from Animal a " +
                "WHERE a.belongsTo.id = " + farmId.toString();

        if (tokensName.length > 0 || startDate != null || endDate != null || identificationNumber != null || isMale != null || dead != null) {
            queryString += " AND ";
        }

        // name
        if (tokensName.length > 0) {
            for (int i = 0; i < tokensName.length-1; i++) {
                queryString += "LOWER(a.name) LIKE LOWER(:token" + i + ") AND ";
            }
            queryString += "LOWER(a.name) LIKE LOWER(:token" + (tokensName.length-1) + ") ";
        }

        // identificationNumber
        if (identificationNumber != null) {
            if (tokensName.length > 0) {
                queryString += " AND ";
            }
            queryString += "a.identificationNumber = :identificationNumber ";
        }

        // startDate
        if (startDate != null) {
            if (tokensName.length > 0 || identificationNumber != null) {
                queryString += " AND ";
            }
            queryString += "a.birthDate >= :startDate ";
        }

        // endDate
        if (endDate != null) {
            if (tokensName.length > 0 || identificationNumber != null || startDate != null) {
                queryString += " AND ";
            }
            queryString += "a.birthDate <= :endDate ";
        }

        // isMale
        if (isMale != null) {
            if (tokensName.length > 0 || identificationNumber != null || startDate != null || endDate != null) {
                queryString += " AND ";
            }
            queryString += "a.isMale = :isMale ";
        }

        // dead
        if (dead != null) {
            if (tokensName.length > 0 || identificationNumber != null || startDate != null || endDate != null || isMale != null) {
                queryString += " AND ";
            }
            queryString += "a.isDead = :dead ";
        }

        queryString += " ORDER BY a.birthDate DESC";
        Query query = entityManager.createQuery(queryString).setFirstResult(page*size).setMaxResults(size+1);

        // name
        if (tokensName.length > 0) {
            for (int i = 0; i < tokensName.length; i++) {
                query.setParameter("token" + i, '%'+tokensName[i]+'%');
            }
        }

        // identificationNumber
        if (identificationNumber != null) {
            query.setParameter("identificationNumber", identificationNumber);
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

        // isMale
        if (isMale != null) {
            query.setParameter("isMale", isMale);
        }

        // dead
        if (dead != null) {
            query.setParameter("dead", dead);
        }

        List<Animal> animals = query.getResultList();
        boolean hasNext = animals.size() == (size+1);

        if (hasNext) {
            animals.remove(animals.size()-1);
        }

        return new SliceImpl<>(animals, PageRequest.of(page, size), hasNext);
    }
}
