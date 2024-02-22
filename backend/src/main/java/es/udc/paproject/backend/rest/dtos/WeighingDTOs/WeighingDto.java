package es.udc.paproject.backend.rest.dtos.WeighingDTOs;

import javax.validation.constraints.NotNull;

public class WeighingDto {
    public interface CreateWeighingValidation {}
    public interface UpdateWeighingValidation {}

    private Long id;
    private Long date;
    private Integer Kilos;
    private Long madeBy;
    private Long animalWeighed;
    private Boolean production;

    public WeighingDto(Long id, Long date, Integer kilos, Long madeBy, Long animalWeighed, Boolean production) {
        this.id = id;
        this.date = date;
        Kilos = kilos;
        this.madeBy = madeBy;
        this.animalWeighed = animalWeighed;
        this.production = production;
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

    @NotNull(groups = {CreateWeighingValidation.class, UpdateWeighingValidation.class})
    public Integer getKilos() {
        return Kilos;
    }

    public void setKilos(Integer kilos) {
        Kilos = kilos;
    }

    public Long getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(Long madeBy) {
        this.madeBy = madeBy;
    }

    @NotNull(groups = {CreateWeighingValidation.class, UpdateWeighingValidation.class})
    public Long getAnimalWeighed() {
        return animalWeighed;
    }

    public void setAnimalWeighed(Long animalWeighed) {
        this.animalWeighed = animalWeighed;
    }

    public Boolean getProduction() {
        return production;
    }

    public void setProduction(Boolean production) {
        this.production = production;
    }
}
