package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "managers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "deleted", allowGetters = true)
public class Manager extends com.example.Entity {

    @NotNull(message = "Choose your side!")
    private ManagerTypes managerType;

    @NotBlank(message = "Are you from another country?")
    private String fullName;

    @NotNull(message = "Age can not be empty")
    private int age;

    @NotNull(message = "Each person has male!")
    private Male male;

    public Manager(){}

    public Manager(@NotBlank ManagerTypes managerType){
        this.managerType = managerType;
    }
    public Manager(@NotBlank String fullName, @NotBlank int age, Male male, ManagerTypes managerType){
        this(managerType);
        setFullName(fullName);
        setMale(male);
        setAge(age);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Male getMale() {
        return male;
    }

    public void setMale(Male male) {
        this.male = male;
    }

    public ManagerTypes getManagerType() {
        return managerType;
    }

    public void setManagerType(ManagerTypes managerType) {
        this.managerType = managerType;
    }

}
