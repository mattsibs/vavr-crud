package com.dm.vavr.crud.service;

import com.dm.vavr.crud.data.Identifiable;
import com.dm.vavr.crud.service.failure.NotFoundFailure;
import com.google.common.collect.ImmutableList;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CRUDServiceTest {

    /*
     * Tests for the default methods in CRUDService, verification of non default methods has not been
     * included in these tests.
     */

    @Spy
    private CRUDService<TestEntity, String, Integer, Integer> crudService;

    @Before
    public void setUp() throws Exception {
        when(crudService.entityName())
                .thenReturn("testEntity");
        doCallRealMethod()
                .when(crudService)
                .findById(anyString());
    }

    @Test
    public void findById_entityNotFound_ReturnsCRUDFailure() throws Exception {
        when(crudService._findById("id"))
                .thenReturn(Option.none());
        when(crudService.entityName())
                .thenReturn("testEntity");

        assertThat(crudService.findById("id"))
                .isEqualTo(Either.left(NotFoundFailure.one("testEntity", "id")));
    }

    @Test
    public void findById_entityFound_ReturnsEntity() throws Exception {
        TestEntity testEntity = new TestEntity("hello", 0);

        when(crudService._findById("id"))
                .thenReturn(Option.of(testEntity));

        assertThat(crudService.findById("id"))
                .isEqualTo(Either.right(testEntity));
    }

    @Test
    public void delete_entityNotFound_ReturnsCRUDFailure() throws Exception {
        when(crudService._findById("id"))
                .thenReturn(Option.none());
        when(crudService.entityName())
                .thenReturn("testEntity");

        assertThat(crudService.delete("id"))
                .isEqualTo(Either.left(NotFoundFailure.one("testEntity", "id")));
    }

    @Test
    public void delete_entityFound_ReturnsEntity() throws Exception {
        TestEntity testEntity = new TestEntity("hello", 0);

        when(crudService._findById("id"))
                .thenReturn(Option.of(testEntity));

        when(crudService._delete(any()))
                .thenReturn(testEntity);

        assertThat(crudService.delete("id"))
                .isEqualTo(Either.right(testEntity));
    }

    @Test
    public void findByIds_SomeNotFound_ReturnsFailure() throws Exception {
        TestEntity testEntity1 = new TestEntity("id1", 1);
        TestEntity testEntity2 = new TestEntity("id2", 2);
        TestEntity testEntity3 = new TestEntity("id3", 3);
        TestEntity testEntity4 = new TestEntity("id4", 4);
        TestEntity testEntity5 = new TestEntity("id5", 5);

        when(crudService._findByIds(any()))
                .thenReturn(ImmutableList.of(testEntity1, testEntity2, testEntity3, testEntity4, testEntity5));

        when(crudService.entityName())
                .thenReturn("entity");

        assertThat(crudService.findByIds(ImmutableList.of("id1", "id6", "id7")))
                .isEqualTo(Either.left(
                        NotFoundFailure.many("entity", ImmutableList.of("id6", "id7"))));
    }

    @Test
    public void findByIds_AllFound_ReturnsEntities() throws Exception {
        TestEntity testEntity1 = new TestEntity("id1", 1);
        TestEntity testEntity2 = new TestEntity("id2", 2);

        when(crudService._findByIds(any()))
                .thenReturn(ImmutableList.of(testEntity1, testEntity2));

        assertThat(crudService.findByIds(ImmutableList.of("id1", "id2")))
                .isEqualTo(Either.right(
                        ImmutableList.of(testEntity1, testEntity2)));
    }

    @Test
    public void update_entityNotFound_ReturnsCRUDFailure() throws Exception {
        when(crudService._findById("id"))
                .thenReturn(Option.none());

        when(crudService.entityName())
                .thenReturn("testEntity");

        assertThat(crudService.update("id", 100))
                .isEqualTo(Either.left(NotFoundFailure.one("testEntity", "id")));
    }

    @Test
    public void update_entityFound_ReturnsEntity() throws Exception {
        TestEntity testEntity = new TestEntity("hello", 0);

        when(crudService._findById("id"))
                .thenReturn(Option.of(testEntity));

        when(crudService._update(any(), any()))
                .thenReturn(testEntity);

        assertThat(crudService.update("id", 100))
                .isEqualTo(Either.right(testEntity));
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