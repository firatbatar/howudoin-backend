package edu.sabanciuniv.howudoin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {
    private final Status status;
    private final String message;
    private final Object data;

    public enum Status {
        SUCCESS,
        ERROR
    }
}
