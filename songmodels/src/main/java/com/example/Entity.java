package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
    public abstract class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    private boolean isDeleted;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public Entity(){}

    public void delete(){
        isDeleted = true;
    }
    public boolean getDeleted(){
        return isDeleted;
    }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();

            String jsonString = "";
            try {
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                jsonString = mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return jsonString;
        }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

