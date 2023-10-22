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

public class CustomizedIssueDaoImpl implements CustomizedIssueDao{
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
    public Slice<Issue> find(Long farmId, String issueName, String startDate, String endDate, Boolean isDone, int page, int size) {

        String[] tokens = getTokens(issueName);
        String queryString = "SELECT i from Issue i " +
                "left join User u on i.createdBy.id = u.id " +
                "WHERE u.farm.id = " + farmId.toString();

        if (tokens.length > 0 || startDate != null || endDate != null || isDone != null){
            queryString += " AND ";
        }

        // issueName
        if (tokens.length > 0) {

            for (int i = 0; i < tokens.length-1; i++) {
                queryString += "LOWER(i.issueName) LIKE LOWER(:token" + i + ") AND ";
            }

            queryString += "LOWER(i.issueName) LIKE LOWER(:token" + (tokens.length-1) + ") ";
        }

        // startTime
        if (startDate != null) {
            if (tokens.length > 0) {
                queryString += "AND ";
            }
            queryString += "i.creationDate > :startDate ";
        }

        // endTime
        if (endDate != null) {
            if (tokens.length > 0 || startDate != null) {
                queryString += " AND ";
            }
            queryString += "i.creationDate < :endDate ";
        }

        // isDone
        if (isDone != null) {
            if (tokens.length > 0 || startDate != null || endDate != null) {
                queryString += " AND ";
            }
            queryString += "i.isDone = :isDone ";
        }

        queryString += " ORDER BY i.creationDate DESC";

        Query query = entityManager.createQuery(queryString).setFirstResult(page*size).setMaxResults(size+1);

        // issueName
        if (tokens.length > 0) {
            for (int i = 0; i < tokens.length; i++) {
                query.setParameter("token" + i, '%'+tokens[i]+'%');
            }
        }

        // startDate
        if (startDate != null) {
            //query.setParameter("startTime", startTime.toString().substring(0,10));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime sd = LocalDateTime.parse(startDate + " 23:59:59", formatter);
            query.setParameter("startDate", sd);
        }

        // endTime
        if (endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ed = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            query.setParameter("endDate", ed);
        }

        // isDone
        if (isDone != null) {
            query.setParameter("isDone", isDone);
        }

        List<Issue> issues = query.getResultList();
        boolean hasNext = issues.size() == (size+1);

        if (hasNext) {
            issues.remove(issues.size()-1);
        }

        return new SliceImpl<>(issues, PageRequest.of(page, size), hasNext);
    }
}
