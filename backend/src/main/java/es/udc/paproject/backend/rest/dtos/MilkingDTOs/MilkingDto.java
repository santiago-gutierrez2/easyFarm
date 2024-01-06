package es.udc.paproject.backend.rest.dtos.MilkingDTOs;

import javax.validation.constraints.NotNull;

public class MilkingDto {
    public interface CreateMilkingValidation {}
    public interface UpdateMilkingValidation {}

    private Long id;
    private Long date;
    private Integer liters;
    private Long madeBy;
    private Long animalMilked;

    public MilkingDto(Long id, Long date, Integer liters, Long madeBy, Long animalMilked) {
        this.id = id;
        this.date = date;
        this.liters = liters;
        this.madeBy = madeBy;
        this.animalMilked = animalMilked;
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

    @NotNull(groups = {CreateMilkingValidation.class, UpdateMilkingValidation.class})
    public Integer getLiters() {
        return liters;
    }

    public void setLiters(Integer liters) {
        this.liters = liters;
    }

    public Long getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(Long madeBy) {
        this.madeBy = madeBy;
    }

    @NotNull(groups = {CreateMilkingValidation.class, UpdateMilkingValidation.class})
    public Long getAnimalMilked() {
        return animalMilked;
    }

    public void setAnimalMilked(Long animalMilked) {
        this.animalMilked = animalMilked;
    }
}
