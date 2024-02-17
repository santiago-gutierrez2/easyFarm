package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.FoodPurchase;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.IncorrectKilosValueException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.FoodPurchaseService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.common.ErrorsDto;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseConversor;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseWithAvailableKilosDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/foodPurchase")
public class FoodPurchaseController {

    private final static String INCORRECT_KILOS_VALUE_EXCEPTION_CODE = "project.exception.IncorrectKilosValueException";

    @Autowired
    private FoodPurchaseService foodPurchaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(IncorrectKilosValueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorsDto handleIncorrectKilosValueException(IncorrectKilosValueException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(INCORRECT_KILOS_VALUE_EXCEPTION_CODE,
                new Object[] {exception.getKilos().toString()},
                INCORRECT_KILOS_VALUE_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

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
        throws InstanceNotFoundException, IncorrectKilosValueException {

        return FoodPurchaseConversor.toFoodPurchaseDto(foodPurchaseService.updateFoodPurchase(id, foodPurchaseDto.getProductName(),
                foodPurchaseDto.getIngredients(), foodPurchaseDto.getSupplier(), foodPurchaseDto.getKilos(), foodPurchaseDto.getUnitPrice()));
    }

    @DeleteMapping("/{id}/deleteFoodPurchase")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
         @RequestParam(required = false) String productName, @RequestParam(required = false) String supplier, @RequestParam(required = false) String startDate,
         @RequestParam(required = false) String endDate) throws InstanceNotFoundException {

        Block<FoodPurchase> foodPurchaseBlock = foodPurchaseService.getAllFoodPurchases(userId, productName, supplier, startDate, endDate, page, size);

        return new BlockDto<>(FoodPurchaseConversor.toFoodPurchaseDtos(foodPurchaseBlock.getItems()), foodPurchaseBlock.getExistMoreItems());
    }

    @GetMapping("/getListOfFoodPurchases")
    public List<FoodPurchaseDto> getListOfAllFoodPurchases(@RequestAttribute Long userId) throws InstanceNotFoundException {
        return FoodPurchaseConversor.toFoodPurchaseDtos(foodPurchaseService.getListOfAllFoodPurchases(userId));
    }

    @GetMapping("/getAvailableFoodBatches")
    public List<FoodPurchaseWithAvailableKilosDto> getAvailableFoodPurchases(@RequestAttribute Long userId) throws InstanceNotFoundException {
        return foodPurchaseService.getAllAvailablesFoodBatches(userId);
    }
}
