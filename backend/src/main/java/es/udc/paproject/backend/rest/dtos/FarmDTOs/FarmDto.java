package es.udc.paproject.backend.rest.dtos.FarmDTOs;

public class FarmDto {

    private Long id;
    private String name;
    private String address;
    private Integer sizeHectares;
    private Long creationDate;

    public FarmDto() {}

    public FarmDto(Long id, String name, String address, Integer sizeHectares, Long creationDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.sizeHectares = sizeHectares;
        this.creationDate = creationDate;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSizeHectares() {
        return sizeHectares;
    }

    public void setSizeHectares(Integer sizeHectares) {
        this.sizeHectares = sizeHectares;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }
}
