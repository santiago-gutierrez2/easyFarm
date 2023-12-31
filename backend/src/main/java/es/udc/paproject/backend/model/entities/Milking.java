package es.udc.paproject.backend.model.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Milking {

    private Long id;
    private LocalDateTime date;
    private Integer liters;
    private User madeBy;
    private Animal animalMilked;

    // empty constructor
    public Milking() {}

    // main constructor

    public Milking(Integer liters, User madeBy, Animal animalMilked) {
        this.liters = liters;
        this.madeBy = madeBy;
        this.animalMilked = animalMilked;
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

    public Integer getLiters() {
        return liters;
    }

    public void setLiters(Integer liters) {
        this.liters = liters;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "madeBy")
    public User getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(User madeBy) {
        this.madeBy = madeBy;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animalMilked")
    public Animal getAnimalMilked() {
        return animalMilked;
    }

    public void setAnimalMilked(Animal animalMilked) {
        this.animalMilked = animalMilked;
    }
}
