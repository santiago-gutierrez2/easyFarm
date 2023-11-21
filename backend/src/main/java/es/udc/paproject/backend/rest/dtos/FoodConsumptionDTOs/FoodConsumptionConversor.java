package es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs;

import es.udc.paproject.backend.model.entities.FoodConsumption;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseConversor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class FoodConsumptionConversor {

    private FoodConsumptionConversor() {}

    public final static FoodConsumptionDto toFoodConsumptionDto(FoodConsumption foodConsumption) {
        return new FoodConsumptionDto(foodConsumption.getId(), toMillis(foodConsumption.getDate()), foodConsumption.getKilos(),
                foodConsumption.getFoodBatch().getId(), foodConsumption.getConsumedBy().getId());
    }

    public final static FoodConsumption toFoodConsumption(FoodConsumptionDto foodConsumptionDto) {
        return new FoodConsumption(foodConsumptionDto.getKilos(), null, null);
    }

    public final static List<FoodConsumptionDto> toFoodConsumptionDtos(List<FoodConsumption> foodConsumptions) {
        return foodConsumptions.stream().map(FoodConsumptionConversor::toFoodConsumptionDto).collect(Collectors.toList());
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
