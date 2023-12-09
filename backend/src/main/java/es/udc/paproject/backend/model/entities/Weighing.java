package es.udc.paproject.backend.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Weighing {

    private Long id;
    private LocalDateTime date;
    private Integer kilos;
    private User madeBy;
    private Animal animalWeighed;

    // empty constructor
    public Weighing() {}

    // main constructor

    public Weighing(Integer kilos, User madeBy, Animal animalWeighed) {
        this.kilos = kilos;
        this.madeBy = madeBy;
        this.animalWeighed = animalWeighed;
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
    @JoinColumn(name = "madeBy")
    public User getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(User madeBy) {
        this.madeBy = madeBy;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animalWeighed")
    public Animal getAnimalWeighed() {
        return animalWeighed;
    }

    public void setAnimalWeighed(Animal animalWeighed) {
        this.animalWeighed = animalWeighed;
    }
}
