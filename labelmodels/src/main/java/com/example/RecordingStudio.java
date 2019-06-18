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
@Table(name = "recording_studio")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "deleted", allowGetters = true)
public class RecordingStudio extends com.example.Entity {

    @NotBlank(message = "Studio must has name")
    private String name;

    @NotBlank(message = "How you want to find it?")
    private String address;

    @NotNull(message = "Really, free?))")
    private int rent;

    public RecordingStudio() {}

    public RecordingStudio(@NotBlank String name, @NotBlank String address, @NotNull int rent)
    {
        this.rent = rent;
        this.address = address;
        this.name = name;
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

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }
}
