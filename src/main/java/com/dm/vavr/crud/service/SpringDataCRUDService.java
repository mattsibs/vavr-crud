package com.dm.vavr.crud.service;

import com.dm.vavr.crud.data.Identifiable;
import io.vavr.control.Option;
import org.springframework.data.repository.CrudRepository;

public interface SpringDataCRUDService<T extends Identifiable<ID>, ID, DTO> extends CRUDService<T, ID, DTO, DTO> {

    CrudRepository<T, ID> repository();

    @Override
    default Option<T> _findById(final ID id) {
        return Option.ofOptional(
                repository().findById(id));
    }

    @Override
    default T _delete(final T t) {
        repository().delete(t);
        return t;
    }

    @Override
    default Iterable<T> findAll() {
        return repository().findAll();
    }

    @Override
    default Iterable<T> _findByIds(final Iterable<ID> ids) {
        return repository().findAllById(ids);
    }
}
