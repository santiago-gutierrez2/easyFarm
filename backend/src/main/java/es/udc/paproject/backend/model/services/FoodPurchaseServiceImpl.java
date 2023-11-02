package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class FoodPurchaseServiceImpl implements FoodPurchaseService{

    @Autowired
    private FoodPurchaseDao foodPurchaseDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void createFoodPurchase(FoodPurchase foodPurchase) {
        foodPurchaseDao.save(foodPurchase);
    }

    @Override
    public FoodPurchase updateFoodPurchase(Long id, String productName, String ingredients, String supplier,
       Integer kilos, BigDecimal unitPrice) throws InstanceNotFoundException {
        Optional<FoodPurchase> optionalFoodPurchase = foodPurchaseDao.findById(id);

        if (optionalFoodPurchase.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", id);
        }

        FoodPurchase foodPurchase = optionalFoodPurchase.get();
        foodPurchase.setProductName(productName); // not null
        foodPurchase.setIngredients(ingredients);
        foodPurchase.setSupplier(supplier);
        foodPurchase.setKilos(kilos); // not null
        foodPurchase.setUnitPrice(unitPrice); // not null

        foodPurchaseDao.save(foodPurchase);

        return foodPurchase;
    }

    @Override
    public void deleteFoodPurchase(Long userId, Long id) throws InstanceNotFoundException, PermissionException {
        Optional<FoodPurchase> optionalFoodPurchase = foodPurchaseDao.findById(id);
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalFoodPurchase.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", id);
        } else if (optionalUser.isEmpty() || (optionalUser.get().getRole() != User.RoleType.ADMIN &&
                !Objects.equals(optionalFoodPurchase.get().getMadeBy().getId(), optionalUser.get().getId()))) {
            throw new PermissionException();
        }

        foodPurchaseDao.delete(optionalFoodPurchase.get());
    }

    @Override
    public FoodPurchase getFoodPurchaseById(Long userId, Long id) throws InstanceNotFoundException {

        Optional<FoodPurchase> optionalFoodPurchase = foodPurchaseDao.findById(id);

        if (optionalFoodPurchase.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", id);
        }

        return optionalFoodPurchase.get();
    }

    @Override
    public Block<FoodPurchase> getAllFoodPurchases(Long userId, String productName, String supplier, String startDate, String endDate,
        int page, int size) throws InstanceNotFoundException {

        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        Slice<FoodPurchase> foodPurchaseSlice;
        if (productName != null || supplier != null || startDate != null || endDate != null) {
            foodPurchaseSlice = foodPurchaseDao.find(farm.getId(), productName, startDate, endDate, supplier, page, size);
        } else {
            foodPurchaseSlice = foodPurchaseDao.findByMadeByFarmIdOrderByPurchaseDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        List<FoodPurchase> foodPurchaseList = foodPurchaseSlice.getContent();

        return new Block<>(foodPurchaseList, foodPurchaseSlice.hasNext());
    }
}
