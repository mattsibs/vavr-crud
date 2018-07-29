package com.dm.vavr.crud.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable<ID> {

    ID getId();

    @JsonIgnore
    default Identifiable<ID> toIdentifiable() {
        return this;
    }
}
