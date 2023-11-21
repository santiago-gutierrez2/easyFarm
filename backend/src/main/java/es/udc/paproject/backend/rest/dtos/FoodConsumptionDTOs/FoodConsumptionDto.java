package es.udc.paproject.backend.rest.dtos.FoodConsumptionDTOs;

import javax.validation.constraints.NotNull;

public class FoodConsumptionDto {

    public interface CreateFoodConsumptionValidation {}
    public interface UpdateFoodConsumptionValidation {}

    private Long id;
    private Long date;
    private Integer kilos;
    private Long foodBatch;
    private Long consumedBy;

    public FoodConsumptionDto(Long id, Long date, Integer kilos, Long foodBatch, Long consumedBy) {
        this.id = id;
        this.date = date;
        this.kilos = kilos;
        this.foodBatch = foodBatch;
        this.consumedBy = consumedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @NotNull(groups = {CreateFoodConsumptionValidation.class, UpdateFoodConsumptionValidation.class})
    public Integer getKilos() {
        return kilos;
    }

    public void setKilos(Integer kilos) {
        this.kilos = kilos;
    }

    @NotNull(groups = {CreateFoodConsumptionValidation.class, UpdateFoodConsumptionValidation.class})
    public Long getFoodBatch() {
        return foodBatch;
    }

    public void setFoodBatch(Long foodBatch) {
        this.foodBatch = foodBatch;
    }

    @NotNull(groups = {CreateFoodConsumptionValidation.class, UpdateFoodConsumptionValidation.class})
    public Long getConsumedBy() {
        return consumedBy;
    }

    public void setConsumedBy(Long consumedBy) {
        this.consumedBy = consumedBy;
    }
}
