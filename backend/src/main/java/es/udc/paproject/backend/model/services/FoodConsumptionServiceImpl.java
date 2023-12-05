package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodConsumptionServiceImpl implements FoodConsumptionService{

    @Autowired
    private FoodConsumptionDao foodConsumptionDao;
    @Autowired
    private UserService userService;
    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserDao userDao;

    @Override
    public void createFoodConsumption(FoodConsumption foodConsumption) {
        foodConsumptionDao.save(foodConsumption);
    }

    @Override
    public void createSeveralFoodConsumption(List<FoodConsumption> foodConsumptions) {
        foodConsumptionDao.saveAll(foodConsumptions);
    }

    @Override
    public void updateFoodConsumption(Long id, Integer kilos) throws InstanceNotFoundException {
        Optional<FoodConsumption> optionalFoodConsumption = foodConsumptionDao.findById(id);

        if (optionalFoodConsumption.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", id);
        }

        FoodConsumption foodConsumption = optionalFoodConsumption.get();
        foodConsumption.setKilos(kilos);

        foodConsumptionDao.save(foodConsumption);
    }

    @Override
    public void deleteFoodConsumption(Long id) throws InstanceNotFoundException {
        Optional<FoodConsumption> optionalFoodConsumption = foodConsumptionDao.findById(id);

        if (optionalFoodConsumption.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", id);
        }

        foodConsumptionDao.delete(optionalFoodConsumption.get());
    }

    @Override
    public FoodConsumption getFoodConsumptionById(Long id) throws InstanceNotFoundException {
        Optional<FoodConsumption> optionalFoodConsumption = foodConsumptionDao.findById(id);

        if (optionalFoodConsumption.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", id);
        }

       return optionalFoodConsumption.get();
    }

    @Override
    public Block<FoodConsumption> getAllFoodConsumptions(Long userId, Long animalId, Long foodBatchId, String startDate, String endDate, int page, int size) throws InstanceNotFoundException {

        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        Slice<FoodConsumption> foodConsumptionsSlice;
        if (animalId != null || foodBatchId != null || startDate != null || endDate != null) {
            foodConsumptionsSlice = null;
        } else {
            foodConsumptionsSlice =  foodConsumptionDao.findByConsumedByBelongsToIdOrderByDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        return null;
    }
}
