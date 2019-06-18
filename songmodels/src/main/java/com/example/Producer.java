package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "producers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Producer extends com.example.Entity {

    @NotBlank(message = "Are you from another country?")
    public String fullName;

    @NotNull
    private int experience;

    @NotNull(message = "Age can not be empty")
    public int age;

    @NotNull(message = "Each person has male!")
    public Male male = Male.FEMALE;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Singer> singers = new ArrayList<>();

    public Producer() {}

    public Producer(@NotBlank String fullName,  int age,  int experience, Male male)
    {
        setFullName(fullName);
        setAge(age);
        setMale(male);
        setExperience(experience );
    }

    public Producer(@NotBlank String fullName, @NotNull int age, @NotNull int experience, Singer singer)
    {
        setFullName(fullName);
        setAge(age);
        setMale(Male.MALE);
        setExperience(experience );
        setSinger(singer);
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

    public List<Singer> addSinger(Singer singer) {
        if (singers.contains(singer))
            throw new RuntimeException("Singer already at this producer!");
        singers.add(singer);
        return singers;
    }

    public boolean removeSinger(Singer singer) {
        return singers.contains(singer) && singers.remove(singer);
    }

    public boolean removeSinger(Integer id) {
        for (int i = 0; i < singers.size(); ++i) {
            if (singers.get(i).getId() == id)
                return singers.remove(singers.get(i));
        }
        return false;
    }

    public void  setSinger(Singer singer)   {
        List<Singer> singers = getSingers();
        singers.add(singer);
    }

    public Singer getSinger (Integer singerId) {
        for (Singer s :
                singers) {
            if (s.getId() == singerId)
                return s;
        }
        return getSinger(singerId);
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    public void setSingers(List<Singer> singers) {
        this.singers = singers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producer producer = (Producer) o;

        if (experience != producer.experience) return false;
        if (age != producer.age) return false;
        if (!fullName.equals(producer.fullName)) return false;
        return male == producer.male;
    }

    @Override
    public int hashCode() {
        int result = fullName.hashCode();
        result = 31 * result + experience;
        result = 31 * result + age;
        result = 31 * result + male.hashCode();
        return result;
    }
}
