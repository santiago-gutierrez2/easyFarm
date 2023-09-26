package es.udc.paproject.backend.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Animal {

    private Long id;
    private String name;
    private Long identificationNumber;
    private LocalDateTime birthDate;
    private Boolean isMale;
    private String physicalDescription;
    private Farm belongsTo;
    private Boolean isDead;

    // empty constructor
    public Animal() {}

    // main constructor
    public Animal(String name, Long identificationNumber, LocalDateTime birthDate, Boolean isMale, String physicalDescription,
                  Farm belongsTo, Boolean isDead) {
        this.name = name;
        this.identificationNumber = identificationNumber;
        this.birthDate = birthDate;
        this.isMale = isMale;
        this.physicalDescription = physicalDescription;
        this.belongsTo = belongsTo;
        this.isDead = isDead;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    public String getPhysicalDescription() {
        return physicalDescription;
    }

    public void setPhysicalDescription(String physicalDescription) {
        this.physicalDescription = physicalDescription;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Farm getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Farm belongsTo) {
        this.belongsTo = belongsTo;
    }

    public Boolean getDead() {
        return isDead;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }
}
