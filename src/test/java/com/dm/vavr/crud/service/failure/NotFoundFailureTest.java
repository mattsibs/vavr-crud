package com.dm.vavr.crud.service.failure;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class NotFoundFailureTest {

    @Test
    public void errorMessage_many_returnsErrorMessageWithIds() throws Exception {
        NotFoundFailure failure = NotFoundFailure.many("entity", ImmutableList.of(1, 2, 3, 4, 5));

        assertThat(failure.errorMessage())
                .isEqualTo("Could not find entity for given ids [1, 2, 3, 4, 5]");
    }

    @Test
    public void errorMessage_one_returnsErrorMessageWithIds() throws Exception {
        NotFoundFailure failure = NotFoundFailure.one("entity", 1);

        assertThat(failure.errorMessage())
                .isEqualTo("Could not find entity for given ids [1]");
    }

    @Test
    public void errorCode_returnsNotFound() throws Exception {
        NotFoundFailure failure = NotFoundFailure.one("entity", 1);

        assertThat(failure.errorCode())
                .isEqualTo("ENTITY_NOT_FOUND");
    }
}