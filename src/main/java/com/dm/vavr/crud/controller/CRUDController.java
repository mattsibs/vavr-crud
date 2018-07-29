package com.dm.vavr.crud.controller;

import com.dm.vavr.crud.service.failure.Error;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Collections.singletonList;

public interface CRUDController<T, ID> {

    default ResponseEntity toBadRequest(final Error error) {
        return toBadRequest(singletonList(error));
    }

    default ResponseEntity toBadRequest(final List<Error> errors) {
        return ResponseEntity.badRequest()
                .body(errors);
    }
}
