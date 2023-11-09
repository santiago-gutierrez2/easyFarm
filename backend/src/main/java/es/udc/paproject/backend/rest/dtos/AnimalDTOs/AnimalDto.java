package es.udc.paproject.backend.rest.dtos.AnimalDTOs;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AnimalDto {

    public interface CreateAnimalValidation {}
    public interface UpdateAnimalValidation {}

    private Long id;
    private String name;
    private Long identificationNumber;
    private Long birthDate;
    private Boolean isMale;
    private String physicalDescription;
    private Long belongsTo;
    private Boolean isDead;
    private String birthDateString;

    public AnimalDto(Long id, String name, Long identificationNumber, Long birthDate, Boolean isMale, String physicalDescription, Long belongsTo, Boolean isDead) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.identificationNumber = identificationNumber;
        this.birthDate = birthDate;
        this.isMale = isMale;
        this.physicalDescription = physicalDescription;
        this.belongsTo = belongsTo;
        this.isDead = isDead;
    }

    public AnimalDto(Long id, String name, Long identificationNumber, String birthDate, Boolean isMale, String physicalDescription, Long belongsTo, Boolean isDead) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.identificationNumber = identificationNumber;
        this.birthDateString = birthDate;
        this.isMale = isMale;
        this.physicalDescription = physicalDescription;
        this.belongsTo = belongsTo;
        this.isDead = isDead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull(groups = {CreateAnimalValidation.class, UpdateAnimalValidation.class})
    @Size(min = 1, max=60, groups = {CreateAnimalValidation.class, UpdateAnimalValidation.class})
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(groups = {CreateAnimalValidation.class, UpdateAnimalValidation.class})
    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @NotNull(groups = {CreateAnimalValidation.class, UpdateAnimalValidation.class})
    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    @NotNull(groups = {CreateAnimalValidation.class, UpdateAnimalValidation.class})
    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    @Size(max=250, groups = {CreateAnimalValidation.class, UpdateAnimalValidation.class})
    public String getPhysicalDescription() {
        return physicalDescription;
    }

    public void setPhysicalDescription(String physicalDescription) {
        this.physicalDescription = physicalDescription;
    }

    @NotNull(groups = {CreateAnimalValidation.class})
    public Long getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Long belongsTo) {
        this.belongsTo = belongsTo;
    }

    public Boolean getDead() {
        return isDead;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }

    public String getBirthDateString() {
        return birthDateString;
    }

    public void setBirthDateString(String birthDateString) {
        this.birthDateString = birthDateString;
    }
}
