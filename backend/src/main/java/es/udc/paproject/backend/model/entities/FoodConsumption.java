package es.udc.paproject.backend.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FoodConsumption {

    private Long id;
    private LocalDateTime date;
    private Integer kilos;
    private FoodPurchase foodBatch;
    private Animal consumedBy;

    // empty constructor
    public FoodConsumption() {}

    // main constructor

    public FoodConsumption(Integer kilos, FoodPurchase foodBatch, Animal consumedBy) {
        this.kilos = kilos;
        this.foodBatch = foodBatch;
        this.consumedBy = consumedBy;
        this.date = LocalDateTime.now().withNano(0);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getKilos() {
        return kilos;
    }

    public void setKilos(Integer kilos) {
        this.kilos = kilos;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "foodBatch")
    public FoodPurchase getFoodBatch() {
        return foodBatch;
    }

    public void setFoodBatch(FoodPurchase foodBatch) {
        this.foodBatch = foodBatch;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "consumedBy")
    public Animal getConsumedBy() {
        return consumedBy;
    }

    public void setConsumedBy(Animal consumedBy) {
        this.consumedBy = consumedBy;
    }
}
