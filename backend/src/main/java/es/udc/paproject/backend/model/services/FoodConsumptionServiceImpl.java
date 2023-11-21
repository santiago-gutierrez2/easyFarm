package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.FoodConsumption;
import es.udc.paproject.backend.model.entities.FoodConsumptionDao;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FoodConsumptionServiceImpl implements FoodConsumptionService{

    @Autowired
    private FoodConsumptionDao foodConsumptionDao;
    @Autowired
    private UserService userService;
    @Autowired
    private AnimalService animalService;

    @Override
    public void createFoodConsumption(FoodConsumption foodConsumption) {
        foodConsumptionDao.save(foodConsumption);
    }

    @Override
    public void createSeveralFoodConsumption(List<FoodConsumption> foodConsumptions) {

    }

    @Override
    public void updateFoodConsumption(Long id, FoodConsumption foodConsumption) throws InstanceNotFoundException {

    }

    @Override
    public FoodConsumption getFoodConsumptionById(Long id) throws InstanceNotFoundException {
        return null;
    }

    @Override
    public Block<FoodConsumption> getAllFoodConsumptions(Long userId, Long animalId, String startDate, String endDate, int page, int size) throws InstanceNotFoundException {
        return null;
    }
}
