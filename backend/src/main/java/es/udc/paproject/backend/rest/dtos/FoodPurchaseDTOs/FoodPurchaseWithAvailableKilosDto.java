package es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class FoodPurchaseWithAvailableKilosDto {

    public interface CreateFoodPurchaseValidation {}
    public interface UpdateFoodPurchaseValidation {}

    private Long id;
    private String productName;
    private String ingredients;
    private Long purchaseDate;
    private String supplier;
    private Integer kilos;
    private BigDecimal unitPrice;
    private Long madeBy;
    private Integer availableKilos;


    public FoodPurchaseWithAvailableKilosDto(Long id, String productName, String ingredients, Long purchaseDate, String supplier, Integer kilos, BigDecimal unitPrice, Long madeBy, Integer availableKilos) {
        this.id = id;
        this.productName = productName != null ? productName.trim() : null;
        this.ingredients = ingredients;
        this.purchaseDate = purchaseDate;
        this.supplier = supplier;
        this.kilos = kilos;
        this.unitPrice = unitPrice;
        this.madeBy = madeBy;
        this.availableKilos = availableKilos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull(groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class, FoodPurchaseDto.UpdateFoodPurchaseValidation.class})
    @Size(min=1, max=100, groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class, FoodPurchaseDto.UpdateFoodPurchaseValidation.class})
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Size(max=500, groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class, FoodPurchaseDto.UpdateFoodPurchaseValidation.class})
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Size(max=100, groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class, FoodPurchaseDto.UpdateFoodPurchaseValidation.class})
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @NotNull(groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class, FoodPurchaseDto.UpdateFoodPurchaseValidation.class})
    public Integer getKilos() {
        return kilos;
    }

    public void setKilos(Integer kilos) {
        this.kilos = kilos;
    }

    @NotNull(groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class, FoodPurchaseDto.UpdateFoodPurchaseValidation.class})
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @NotNull(groups = {FoodPurchaseDto.CreateFoodPurchaseValidation.class})
    public Long getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(Long madeBy) {
        this.madeBy = madeBy;
    }

    public Integer getAvailableKilos() {
        return availableKilos;
    }

    public void setAvailableKilos(Integer availableKilos) {
        this.availableKilos = availableKilos;
    }
}
