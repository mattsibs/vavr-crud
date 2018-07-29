package com.dm.vavr.crud.service.failure;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import static java.util.Collections.singletonList;

public class NotFoundFailure implements CRUDFailure {

    private final String errorCode;
    private final String errorMessage;

    private NotFoundFailure(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static <ID> NotFoundFailure one(final String entityName, final ID id) {
        return NotFoundFailure.many(entityName, singletonList(id));
    }

    public static <ID> NotFoundFailure many(final String entityName, final Iterable<ID> ids) {
        String errorCode = String.format("%s_NOT_FOUND", entityName.toUpperCase());

        String errorMessage = String.format("Could not find %s for given ids %s", entityName, ImmutableList.copyOf(ids));

        return new NotFoundFailure(errorCode, errorMessage);
    }

    @Override
    public String errorCode() {
        return errorCode;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotFoundFailure that = (NotFoundFailure) o;
        return Objects.equal(errorCode, that.errorCode) &&
                Objects.equal(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("errorCode", errorCode)
                .add("errorMessage", errorMessage)
                .toString();
    }
}
