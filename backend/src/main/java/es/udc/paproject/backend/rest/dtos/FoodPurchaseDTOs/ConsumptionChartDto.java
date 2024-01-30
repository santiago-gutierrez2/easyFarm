package es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs;

import java.time.LocalDateTime;

public class ConsumptionChartDto {

    private Long kilos;
    private String consumptionDate;
    private Long foodBatchId;

    public ConsumptionChartDto(Long kilos, String consumptionDate, Long foodBatchId) {
        this.kilos = kilos;
        this.consumptionDate = consumptionDate;
        this.foodBatchId = foodBatchId;
    }

    public Long getKilos() {
        return kilos;
    }

    public void setKilos(Long kilos) {
        this.kilos = kilos;
    }

    public String getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(String consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public Long getFoodBatchId() {
        return foodBatchId;
    }

    public void setFoodBatchId(Long foodBatchId) {
        this.foodBatchId = foodBatchId;
    }
}
