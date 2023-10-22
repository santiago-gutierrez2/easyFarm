package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.FoodPurchase;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.FoodPurchaseService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseConversor;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foodPurchase")
public class FoodPurchaseController {

    @Autowired
    private FoodPurchaseService foodPurchaseService;
    @Autowired
    private UserService userService;

    @PostMapping("/createFoodPurchase")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createFoodPurchase(@RequestAttribute Long userId,
        @Validated({FoodPurchaseDto.CreateFoodPurchaseValidation.class}) @RequestBody FoodPurchaseDto foodPurchaseDto)
        throws PermissionException, InstanceNotFoundException {

        User user = userService.loginFromId(userId);

        FoodPurchase foodPurchase = FoodPurchaseConversor.toFoodPurchase(foodPurchaseDto);
        foodPurchase.setMadeBy(user);

        foodPurchaseService.createFoodPurchase(foodPurchase);
    }

    @PutMapping("/{id}")
    public FoodPurchaseDto updateFoodPurchase(@RequestAttribute Long userId, @PathVariable Long id,
        @Validated({FoodPurchaseDto.UpdateFoodPurchaseValidation.class}) @RequestBody FoodPurchaseDto foodPurchaseDto)
        throws InstanceNotFoundException{

        return FoodPurchaseConversor.toFoodPurchaseDto(foodPurchaseService.updateFoodPurchase(id, foodPurchaseDto.getProductName(),
                foodPurchaseDto.getIngredients(), foodPurchaseDto.getSupplier(), foodPurchaseDto.getKilos(), foodPurchaseDto.getUnitPrice()));
    }

    @DeleteMapping("/{id}/deleteFoodPurchase")
    public void deleteFoodPurchase(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException, PermissionException {
        foodPurchaseService.deleteFoodPurchase(userId, id);
    }

    @GetMapping("/{id}")
    public FoodPurchaseDto getFoodPurchaseById(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        FoodPurchase foodPurchase = foodPurchaseService.getFoodPurchaseById(userId, id);
        return FoodPurchaseConversor.toFoodPurchaseDto(foodPurchase);
    }

    @GetMapping("/allFoodPurchases")
    public BlockDto<FoodPurchaseDto> getAllFoodPurchases(@RequestAttribute Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
         @RequestParam(required = false) String productName, @RequestParam(required = false) String startDate,
         @RequestParam(required = false) String endDate, @RequestParam(required = false) Long madeBy) throws InstanceNotFoundException {

        Block<FoodPurchase> foodPurchaseBlock = foodPurchaseService.getAllFoodPurchases(userId, productName, startDate, endDate,
                madeBy, page, size);

        return new BlockDto<>(FoodPurchaseConversor.toFoodPurchaseDtos(foodPurchaseBlock.getItems()), foodPurchaseBlock.getExistMoreItems());
    }
}
