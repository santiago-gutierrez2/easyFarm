package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.FoodConsumptionService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs.FoodConsumptionConversor;
import es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs.FoodConsumptionDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.StockChartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foodConsumption")
public class FoodConsumptionController {

    @Autowired
    private FoodConsumptionService foodConsumptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnimalDao animalDao;
    @Autowired
    private FoodPurchaseDao foodPurchaseDao;

    @PostMapping("/registerFoodConsumption")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createFoodConsumption(@Validated({FoodConsumptionDto.CreateFoodConsumptionValidation.class})
        @RequestBody FoodConsumptionDto foodConsumptionDto) throws InstanceNotFoundException {

        Optional<Animal> optionalAnimal = animalDao.findById(foodConsumptionDto.getConsumedBy());
        Optional<FoodPurchase> optionalFoodPurchase = foodPurchaseDao.findById(foodConsumptionDto.getFoodBatch());

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.animal", foodConsumptionDto.getConsumedBy());
        } else if (optionalFoodPurchase.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", foodConsumptionDto.getFoodBatch());
        }

        Animal animal = optionalAnimal.get();
        FoodPurchase foodPurchase = optionalFoodPurchase.get();

        FoodConsumption foodConsumption = FoodConsumptionConversor.toFoodConsumption(foodConsumptionDto);
        foodConsumption.setFoodBatch(foodPurchase);
        foodConsumption.setConsumedBy(animal);

        foodConsumptionService.createFoodConsumption(foodConsumption);
    }

    @PostMapping("/registerSeveralFoodConsumptions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createSeveralFoodConsumptions(@RequestBody List<FoodConsumptionDto> foodConsumptionDtos) throws InstanceNotFoundException {

        Optional<FoodPurchase> optionalFoodPurchase = foodPurchaseDao.findById(foodConsumptionDtos.get(0).getFoodBatch());

        if (optionalFoodPurchase.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.foodPurchase", foodConsumptionDtos.get(0).getFoodBatch());
        }

        List<FoodConsumption> foodConsumptions = new ArrayList<>();
        for (FoodConsumptionDto foodConsumptionDto : foodConsumptionDtos) {
            Optional<Animal> optionalAnimal = animalDao.findById(foodConsumptionDto.getConsumedBy());
            if (optionalAnimal.isEmpty()) {
                throw new InstanceNotFoundException("project.entities.animal", foodConsumptionDto.getConsumedBy());
            }
            FoodConsumption foodConsumption = FoodConsumptionConversor.toFoodConsumption(foodConsumptionDto);
            foodConsumption.setKilos(foodConsumption.getKilos() / foodConsumptionDtos.size());
            foodConsumption.setFoodBatch(optionalFoodPurchase.get());
            foodConsumption.setConsumedBy(optionalAnimal.get());
            // add to list
            foodConsumptions.add(foodConsumption);
        }

        foodConsumptionService.createSeveralFoodConsumption(foodConsumptions);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFoodConsumption(@PathVariable Long id, @RequestBody FoodConsumptionDto foodConsumptionDto) throws InstanceNotFoundException {
        foodConsumptionService.updateFoodConsumption(id, foodConsumptionDto.getKilos());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFoodConsumption(@PathVariable Long id) throws InstanceNotFoundException {
        foodConsumptionService.deleteFoodConsumption(id);
    }

    @GetMapping("/{id}")
    public FoodConsumptionDto getFoodConsumptionById(@PathVariable Long id) throws InstanceNotFoundException {
        FoodConsumption foodConsumption = foodConsumptionService.getFoodConsumptionById(id);
        return FoodConsumptionConversor.toFoodConsumptionDto(foodConsumption);
    }

    @GetMapping("/allFoodConsumptions")
    public BlockDto<FoodConsumptionDto> getAllFoodConsumptions(@RequestAttribute Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) List<Long> animalsId, @RequestParam(required = false) Long foodBatchId, @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) throws InstanceNotFoundException {

        Block<FoodConsumption> foodConsumptionBlock = foodConsumptionService.getAllFoodConsumptions(userId, animalsId, foodBatchId, startDate, endDate, page, size);

        return new BlockDto<>(FoodConsumptionConversor.toFoodConsumptionDtos(foodConsumptionBlock.getItems()), foodConsumptionBlock.getExistMoreItems());
    }

    @GetMapping("/stockChart")
    public List<StockChartDto> getStockChart(@RequestAttribute Long userId) throws InstanceNotFoundException {
        return foodConsumptionService.getStockChart(userId);
    }

    @GetMapping("/consumptionChart/{id}")
    public List<ConsumptionChartDto> getconsumptionchart(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        return foodConsumptionService.getConsumptionChartData(userId, id);
    }

    @GetMapping("/generalFoodConsumptionChart")
    public List<ConsumptionChartDto> getGeneralConsumptionChart(@RequestAttribute Long userId) throws InstanceNotFoundException {
        return foodConsumptionService.getGeneralConsumptionChartData(userId);
    }

}
