package es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs;

import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.FoodPurchaseDto;

public class StockChartDto {

    private FoodPurchaseDto foodPurchaseDto;
    private Long stockLeft;

    public StockChartDto(FoodPurchaseDto foodPurchaseDto, Long stockLeft) {
        this.foodPurchaseDto = foodPurchaseDto;
        this.stockLeft = stockLeft;
    }

    public FoodPurchaseDto getFoodPurchaseDto() {
        return foodPurchaseDto;
    }

    public void setFoodPurchaseDto(FoodPurchaseDto foodPurchaseDto) {
        this.foodPurchaseDto = foodPurchaseDto;
    }

    public Long getStockLeft() {
        return stockLeft;
    }

    public void setStockLeft(Long stockLeft) {
        this.stockLeft = stockLeft;
    }
}
