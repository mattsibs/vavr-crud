package com.dm.vavr.crud.service.failure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Error<CODE> {

    @JsonProperty
    CODE errorCode();

    @JsonProperty
    String errorMessage();

}
