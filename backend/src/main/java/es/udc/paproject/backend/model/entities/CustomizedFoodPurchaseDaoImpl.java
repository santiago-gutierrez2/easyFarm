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

public class CustomizedFoodPurchaseDaoImpl implements CustomizedFoodPurchaseDao {

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
    public Slice<FoodPurchase> find(Long farmId, String productName, String startDate, String endDate, String supplier, int page, int size) {

        String[] tokensProductName = getTokens(productName);
        String[] tokensSupplier = getTokens(supplier);
        String queryString = "SELECT f from FoodPurchase f " +
                "left join User u on f.madeBy.id = u.id " +
                "WHERE u.farm.id = " + farmId.toString();

        if (tokensProductName.length > 0 || tokensSupplier.length > 0 || startDate != null || endDate != null) {
            queryString += " AND ";
        }

        // productName
        if (tokensProductName.length > 0) {
            for (int i = 0; i < tokensProductName.length-1; i++) {
                queryString += "LOWER(f.productName) LIKE LOWER(:tokenProduct" + i + ") AND ";
            }

            queryString += "LOWER(f.productName) LIKE LOWER(:tokenProduct" + (tokensProductName.length-1) + ") ";
        }

        // suppplier
        if (tokensSupplier.length > 0) {
            if (tokensProductName.length > 0) {
                queryString += "AND ";
            }

            for (int i = 0; i < tokensSupplier.length-1; i++) {
                queryString += "LOWER(f.supplier) LIKE LOWER(:tokenSupplier" + i + ") AND ";
            }

            queryString += "LOWER(f.supplier) LIKE LOWER(:tokenSupplier" + (tokensSupplier.length-1) + ") ";
        }

        // startDate
        if (startDate != null) {
            if (tokensProductName.length > 0 || tokensSupplier.length > 0) {
                queryString += "AND ";
            }

            queryString += "f.purchaseDate > :startDate ";
        }

        // endDate
        if (endDate != null) {
            if (tokensProductName.length > 0 || tokensSupplier.length > 0 || startDate != null) {
                queryString += "AND ";
            }

            queryString += "f.purchaseDate < :endDate ";
        }

        queryString += " ORDER BY f.purchaseDate DESC";

        Query query = entityManager.createQuery(queryString).setFirstResult(page*size).setMaxResults(size+1);

        // productName
        if (tokensProductName.length > 0) {
            for (int i = 0; i < tokensProductName.length; i++) {
                query.setParameter("tokenProduct" + i, '%'+tokensProductName[i]+'%');
            }
        }

        // supplier
        if (tokensSupplier.length > 0) {
            for (int i = 0; i < tokensSupplier.length; i++) {
                query.setParameter("tokenSupplier" + i, '%'+tokensSupplier[i]+'%');
            }
        }

        // startDate
        if (startDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime sd = LocalDateTime.parse(startDate + " 23:59:59", formatter);
            query.setParameter("startDate", sd);
        }

        // endDate
        if (endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ed = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            query.setParameter("endDate", ed);
        }

        List<FoodPurchase> foodPurchases = query.getResultList();
        boolean hasNext = foodPurchases.size() == (size+1);

        if (hasNext) {
            foodPurchases.remove(foodPurchases.size()-1);
        }

        return new SliceImpl<>(foodPurchases, PageRequest.of(page, size), hasNext);
    }
}
