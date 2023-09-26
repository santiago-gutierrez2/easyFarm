package es.udc.paproject.backend.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Farm {

    private Long id;
    private String name;
    private String address;
    private Integer sizeHectares;
    private LocalDateTime creationDate;

    // empty constructor
    public Farm() {}

    // main constructor
    public Farm(String name, String address, Integer sizeHectares) {
        this.name = name;
        this.address = address;
        this.sizeHectares = sizeHectares;
        this.creationDate = LocalDateTime.now().withNano(0);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {return id;}

    public void setId(Long id) { this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public Integer getSizeHectares() {return sizeHectares;}

    public void setSizeHectares(Integer sizeHectares) {this.sizeHectares =sizeHectares;}

    public LocalDateTime getCreationDate() {return creationDate;}

    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
}
