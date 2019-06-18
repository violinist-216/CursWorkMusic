package com.example.Messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String className;
    @NotNull
    private String description;
    @NotNull
    private HttpMethod httpMethod;
    @NotNull
    private String statusCode;
    @NotNull
    @Column(length = 1000)
    private String error;
    @NotNull
    private Timestamp dateTime;

    public Message(Class clas, @NotNull String description, @NotNull HttpMethod httpMethod, ResponseEntity response) {
        this(clas.getName(), description, httpMethod, response.getStatusCode().toString(), new Timestamp(System.currentTimeMillis()), response.getBody().toString());
    }
    public Message(Class clas, @NotNull String description, @NotNull HttpMethod httpMethod, @NotNull String statusCode, @NotNull String error) {
        this(clas.getName(), description, httpMethod, statusCode, new Timestamp(System.currentTimeMillis()), error);
    }
    public Message(String className, String description, HttpMethod httpMethod, ResponseEntity response) {
        this(className, description, httpMethod, response.getStatusCode().toString(), response.getBody().toString());
    }
    public Message(@NotNull String className, @NotNull String description, @NotNull HttpMethod httpMethod, @NotNull String statusCode, @NotNull String error) {
        this(className, description, httpMethod, statusCode, new Timestamp(System.currentTimeMillis()), error);
    }
    public Message(Class clas, String description, HttpMethod httpMethod, String statusCode, Timestamp dateTime, String error) {
        this(clas.getName(), description, httpMethod, statusCode, dateTime, error);
    }
    public Message(String className, String description, HttpMethod httpMethod, String statusCode, Timestamp dateTime, String error) {
        this.className = className;
        this.description = description;
        this.httpMethod = httpMethod;
        this.statusCode = statusCode;
        this.dateTime = dateTime;
        this.error = error;
    }

    public Message(){}

    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
