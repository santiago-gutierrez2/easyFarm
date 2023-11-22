package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.FoodConsumption;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;

import java.util.List;

public interface FoodConsumptionService {

    void createFoodConsumption(FoodConsumption foodConsumption);

    void createSeveralFoodConsumption(List<FoodConsumption> foodConsumptions);

    void updateFoodConsumption(Long id, Integer kilos) throws InstanceNotFoundException;

    void deleteFoodConsumption(Long id) throws InstanceNotFoundException;

    FoodConsumption getFoodConsumptionById(Long id) throws InstanceNotFoundException;

    Block<FoodConsumption> getAllFoodConsumptions(Long userId, Long animalId, Long foodBatchId, String startDate, String endDate,
          int page, int size) throws InstanceNotFoundException;
}
