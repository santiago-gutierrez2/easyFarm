package es.udc.paproject.backend.rest.dtos.WeighingDTOs;

public class WeighingChartDto {

    private Long kilos;
    private String date;
    private Long farmId;

    public WeighingChartDto(Long kilos, String date, Long farmId) {
        this.kilos = kilos;
        this.date = date;
        this.farmId = farmId;
    }

    public Long getKilos() {
        return kilos;
    }

    public void setKilos(Long kilos) {
        this.kilos = kilos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }
}
