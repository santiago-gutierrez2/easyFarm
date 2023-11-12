package es.udc.paproject.backend.rest.dtos.AnimalDTOs;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewAnimalDto {

    public interface CreateAnimalValidation {}
    public interface UpdateAnimalValidation {}

    private Long id;
    private String name;
    private Long identificationNumber;
    private Boolean isMale;
    private String physicalDescription;
    private Long belongsTo;
    private Boolean isDead;
    private String birthDateString;

    public NewAnimalDto(Long id, String name, Long identificationNumber, String birthDateString, Boolean isMale, String physicalDescription, Long belongsTo, Boolean isDead) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.identificationNumber = identificationNumber;
        this.birthDateString = birthDateString;
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

    @NotNull(groups = {AnimalDto.CreateAnimalValidation.class, AnimalDto.UpdateAnimalValidation.class})
    @Size(min = 1, max=60, groups = {AnimalDto.CreateAnimalValidation.class, AnimalDto.UpdateAnimalValidation.class})
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(groups = {AnimalDto.CreateAnimalValidation.class, AnimalDto.UpdateAnimalValidation.class})
    public Long getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(Long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @NotNull(groups = {AnimalDto.CreateAnimalValidation.class, AnimalDto.UpdateAnimalValidation.class})
    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean male) {
        isMale = male;
    }

    @Size(max=250, groups = {AnimalDto.CreateAnimalValidation.class, AnimalDto.UpdateAnimalValidation.class})
    public String getPhysicalDescription() {
        return physicalDescription;
    }

    public void setPhysicalDescription(String physicalDescription) {
        this.physicalDescription = physicalDescription;
    }

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

    @NotNull(groups = {AnimalDto.CreateAnimalValidation.class, AnimalDto.UpdateAnimalValidation.class})
    public String getBirthDateString() {
        return birthDateString;
    }

    public void setBirthDateString(String birthDateString) {
        this.birthDateString = birthDateString;
    }
}
