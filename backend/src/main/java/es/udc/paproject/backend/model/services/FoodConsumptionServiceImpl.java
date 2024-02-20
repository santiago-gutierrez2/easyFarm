package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.StockChartDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseConversor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoodConsumptionServiceImpl implements FoodConsumptionService{

    @Autowired
    private FoodConsumptionDao foodConsumptionDao;
    @Autowired
    private FoodPurchaseDao foodPurchaseDao;
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
    public Block<FoodConsumption> getAllFoodConsumptions(Long userId, List<Long> animalId, Long foodBatchId, String startDate, String endDate, int page, int size) throws InstanceNotFoundException {

        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        Slice<FoodConsumption> foodConsumptionsSlice;
        if ((animalId != null && !animalId.isEmpty()) || foodBatchId != null || startDate != null || endDate != null) {
            foodConsumptionsSlice = foodConsumptionDao.find(farm.getId(), animalId, foodBatchId, startDate, endDate, page, size);
        } else {
            foodConsumptionsSlice =  foodConsumptionDao.findByConsumedByBelongsToIdOrderByDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        List<FoodConsumption> foodConsumptions = foodConsumptionsSlice.getContent();

        return new Block<>(foodConsumptions, foodConsumptionsSlice.hasNext());
    }

    @Override
    public List<StockChartDto> getStockChart(Long userId) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        // foodBatches with stock left.
        List<FoodPurchase> foodPurchasesWithStockLeft = foodPurchaseDao.getAllFoodPurchasesWithStockLeft(farm.getId());

        List<StockChartDto> stockChartDtos = new ArrayList<>();

        foodPurchasesWithStockLeft.forEach(fp -> {
            Long sumOfKilosConsumed = foodPurchaseDao.getAllKilosConsumedByFoodPurchaseId(fp.getId());
            StockChartDto dto = new StockChartDto(FoodPurchaseConversor.toFoodPurchaseDto(fp), fp.getKilos() - sumOfKilosConsumed);
            stockChartDtos.add(dto);
        });

        return stockChartDtos;
    }

    @Override
    public List<ConsumptionChartDto> getConsumptionChartData(Long userId, Long foodBatchId) throws InstanceNotFoundException {

        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();


        return foodConsumptionDao.getConsumptionChartData(foodBatchId);
    }

    @Override
    public List<ConsumptionChartDto> getGeneralConsumptionChartData(Long userId) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        return foodConsumptionDao.getGeneralConsumptionChart(farm.getId());
    }
}
