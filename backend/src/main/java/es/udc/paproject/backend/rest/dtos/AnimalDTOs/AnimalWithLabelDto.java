package es.udc.paproject.backend.rest.dtos.AnimalDTOs;

public class AnimalWithLabelDto {

    private String label;
    private Long value;
    private Boolean disabled;

    public AnimalWithLabelDto(String label, Long value, Boolean disabled) {
        this.label = label;
        this.value = value;
        this.disabled = disabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}

