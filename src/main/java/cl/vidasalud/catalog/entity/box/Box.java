package cl.vidasalud.catalog.entity.box;

import cl.vidasalud.catalog.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "boxes")
public class Box extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 150)
    private String location;

    @Column(length = 50)
    private String floor;

    @Column(nullable = false)
    private Boolean active = true;

    public Box() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getFloor() {
        return floor;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}