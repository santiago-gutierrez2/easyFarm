package es.udc.paproject.backend.model.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class FoodPurchase {

    private Long id;
    private String productName;
    private String ingredients;
    private LocalDateTime purchaseDate;
    private String supplier;
    private Integer kilos;
    private BigDecimal unitPrice;
    private User madeBy;

    // empty constructor
    public FoodPurchase() {}

    // main constructor

    public FoodPurchase(String productName, String ingredients, String supplier, Integer kilos, BigDecimal unitPrice, User madeBy) {
        this.productName = productName;
        this.ingredients = ingredients;
        this.supplier = supplier;
        this.kilos = kilos;
        this.unitPrice = unitPrice;
        this.madeBy = madeBy;
        this.purchaseDate = LocalDateTime.now().withNano(0);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getKilos() {
        return kilos;
    }

    public void setKilos(Integer kilos) {
        this.kilos = kilos;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "madeBy")
    public User getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(User madeBy) {
        this.madeBy = madeBy;
    }
}
