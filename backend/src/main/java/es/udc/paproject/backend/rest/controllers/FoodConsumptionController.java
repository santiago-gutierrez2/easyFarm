package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.services.FoodConsumptionService;
import es.udc.paproject.backend.model.services.FoodPurchaseService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs.FoodConsumptionConversor;
import es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs.FoodConsumptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registrarFoodConsumption")
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
}
