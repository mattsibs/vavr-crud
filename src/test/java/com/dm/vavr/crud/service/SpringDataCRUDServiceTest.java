package com.dm.vavr.crud.service;

import com.dm.vavr.crud.data.Identifiable;
import io.vavr.control.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpringDataCRUDServiceTest {

    @Mock
    private CrudRepository<TestEntity, String> crudRepository;

    @Spy
    private SpringDataCRUDService<TestEntity, String, Integer> springDataCRUDService;

    @Before
    public void setUp() throws Exception {
        when(springDataCRUDService.repository())
                .thenReturn(crudRepository);
    }

    @Test
    public void _findById_found_ReturnsOptionFromRepository() throws Exception {
        TestEntity testEntity = new TestEntity("id1", 100);
        when(crudRepository.findById(any()))
                .thenReturn(Optional.of(testEntity));

        assertThat(springDataCRUDService._findById("id1"))
                .isEqualTo(Option.of(testEntity));
    }

    @Test
    public void _findById_notFound_ReturnsOptionFromRepository() throws Exception {
        when(crudRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThat(springDataCRUDService._findById("id1"))
                .isEqualTo(Option.none());
    }

    @Test
    public void delete_callsDeleteOnRepository() throws Exception {
        TestEntity testEntity = new TestEntity("id1", 100);
        springDataCRUDService._delete(testEntity);

        verify(crudRepository).delete(testEntity);
    }

    @Test
    public void delete_returnsPassedInEntity() throws Exception {
        TestEntity testEntity = new TestEntity("id1", 100);
        TestEntity output = springDataCRUDService._delete(testEntity);

        assertThat(output)
                .isSameAs(testEntity);
    }

    @Test
    public void findAll_callsFindAllOnRepository() throws Exception {
        springDataCRUDService.findAll();

        verify(crudRepository).findAll();
    }

    @Test
    public void findAll_returnsFindAllFromRepository() throws Exception {
        TestEntity testEntity = new TestEntity("id1", 2002);
        when(crudRepository.findAll())
                .thenReturn(singletonList(testEntity));

        Iterable<TestEntity> foundEntities = springDataCRUDService.findAll();

        assertThat(foundEntities)
                .contains(testEntity);

    }

    @Test
    public void findAllById_callsFindAllOnRepository() throws Exception {
        List<String> ids = singletonList("id1");
        springDataCRUDService._findByIds(ids);

        verify(crudRepository).findAllById(ids);
    }

    @Test
    public void findAllById_returnsFindAllFromRepository() throws Exception {
        TestEntity testEntity = new TestEntity("id1", 2002);
        when(crudRepository.findAllById(any()))
                .thenReturn(singletonList(testEntity));

        Iterable<TestEntity> foundEntities = springDataCRUDService._findByIds(singletonList("id"));

        assertThat(foundEntities)
                .contains(testEntity);

    }

    private static class TestEntity implements Identifiable<String> {

        private final String id;
        private Integer attribute;

        private TestEntity(final String id, final Integer attribute) {
            this.id = id;
            this.attribute = attribute;
        }

        @Override
        public String getId() {
            return id;
        }

        public Integer getAttribute() {
            return attribute;
        }

        private void add(final int toAdd) {
            attribute += toAdd;
        }
    }
}