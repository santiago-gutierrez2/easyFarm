package es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs;

import es.udc.paproject.backend.model.entities.FoodPurchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class FoodPurchaseConversor {

    private FoodPurchaseConversor() {}

    public final static FoodPurchaseDto toFoodPurchaseDto(FoodPurchase foodPurchase) {
        return new FoodPurchaseDto(foodPurchase.getId(), foodPurchase.getProductName(), foodPurchase.getIngredients(),
                toMillis(foodPurchase.getPurchaseDate()), foodPurchase.getSupplier(), foodPurchase.getKilos(),
                foodPurchase.getUnitPrice(), foodPurchase.getMadeBy().getId());
    }

    public final static FoodPurchase toFoodPurchase(FoodPurchaseDto foodPurchaseDto) {
        return new FoodPurchase(foodPurchaseDto.getProductName(), foodPurchaseDto.getIngredients(), foodPurchaseDto.getSupplier(),
                foodPurchaseDto.getKilos(), foodPurchaseDto.getUnitPrice(), null);
    }

    public final static List<FoodPurchaseDto> toFoodPurchaseDtos(List<FoodPurchase> foodPurchases) {
        return foodPurchases.stream().map(FoodPurchaseConversor::toFoodPurchaseDto).collect(Collectors.toList());
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
