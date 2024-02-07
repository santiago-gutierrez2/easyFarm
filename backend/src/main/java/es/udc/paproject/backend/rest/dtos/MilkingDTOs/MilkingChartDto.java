package es.udc.paproject.backend.rest.dtos.MilkingDTOs;

public class MilkingChartDto {

    private Long liters;
    private String date;
    private Long animalId;

    public MilkingChartDto(Long liters, String date, Long animalId) {
        this.liters = liters;
        this.date = date;
        this.animalId = animalId;
    }

    public Long getLiters() {
        return liters;
    }

    public void setLiters(Long liters) {
        this.liters = liters;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
}
