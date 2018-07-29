package com.dm.vavr.crud.data;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IdentifiableTest {

    @Test
    public void toIdentifiable_returnsIdentifiableWithId() throws Exception {
        Identifiable<String> test = new TestImpl("id");

        assertThat(test.toIdentifiable().getId())
                .isEqualTo("id");
    }

    private static class TestImpl implements Identifiable<String> {

        private final String id;

        private TestImpl(final String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}