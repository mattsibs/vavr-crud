package com.dm.vavr.crud.service;

import com.dm.vavr.crud.data.Identifiable;
import com.dm.vavr.crud.service.failure.CRUDFailure;
import com.dm.vavr.crud.service.failure.NotFoundFailure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public interface CRUDService<
        ENTITY extends Identifiable<ID>,
        ID,
        CREATE_REQUEST,
        UPDATE_REQUEST> {

    String entityName();

    Option<ENTITY> _findById(final ID id);

    Iterable<ENTITY> findAll();

    Iterable<ENTITY> _findByIds(final Iterable<ID> ids);

    ENTITY _update(final ENTITY ENTITY, final UPDATE_REQUEST updateRequest);

    ENTITY _delete(final ENTITY ENTITY);

    ENTITY create(final CREATE_REQUEST createRequest);

    default Either<CRUDFailure, ENTITY> findById(final ID id) {
        return _findById(id)
                .toEither(NotFoundFailure.one(entityName(), id));
    }

    default Either<CRUDFailure, Iterable<ENTITY>> findByIds(final Iterable<ID> ids) {
        Iterable<ENTITY> byIds = _findByIds(ids);

        Set<ID> foundIds = ImmutableList.copyOf(byIds.iterator()).stream()
                .map(Identifiable::getId)
                .collect(toSet());

        Set<ID> idsNotFound = Sets.difference(ImmutableSet.copyOf(ids), foundIds)
                .copyInto(new HashSet<>());

        if (idsNotFound.isEmpty()) {
            return Either.right(byIds);
        }

        return Either.left(NotFoundFailure.many(entityName(), idsNotFound));
    }

    default Either<CRUDFailure, ENTITY> update(final ID id, final UPDATE_REQUEST updateRequest) {
        Either<CRUDFailure, ENTITY> t = findById(id);
        if (t.isLeft()) {
            return t;
        }

        ENTITY updated = _update(t.get(), updateRequest);
        return Either.right(updated);
    }

    default Either<CRUDFailure, ENTITY> delete(final ID id) {
        Either<CRUDFailure, ENTITY> find = findById(id);

        if (find.isLeft()) {
            return find;
        }

        return Either.right(_delete(find.get()));
    }
}
