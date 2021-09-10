package br.com.zup.orangetalentschallenge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "car_tb")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String model;
    private String year;
    private String Value;
    private String rotationDay;
    private boolean isRotating;
    @ManyToMany(mappedBy = "cars")
    @JsonIgnore
    private List<User> users;
    @OneToMany(mappedBy = "car")
    private List<Register> registers;

}
