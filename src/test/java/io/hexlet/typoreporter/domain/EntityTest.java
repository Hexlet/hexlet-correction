package io.hexlet.typoreporter.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JsonTest
class EntityTest {

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @ValueSource(classes = {Typo.class, Workspace.class})
    void equalsHashCodeVerifier(Class<Identifiable<Long>> clazz) throws Exception {
        final var entityOne = clazz.getConstructor().newInstance();
        final var entityTwo = clazz.getConstructor().newInstance();

        assertThat(entityOne).isEqualTo(entityOne)
            .hasSameHashCodeAs(entityOne)
            .isNotEqualTo(new Object())
            .isNotEqualTo(null)
            .isNotEqualTo(entityTwo)
            .hasSameHashCodeAs(entityTwo);
    }

    @ParameterizedTest
    @ValueSource(classes = {Typo.class, Workspace.class})
    void isSerializeNewEntityToJson(Class<?> clazz) {
        assertDoesNotThrow(() -> objectMapper.writeValueAsString(clazz.getConstructor().newInstance()));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getEntities")
    void isSerializeEntityToJson(final Identifiable<Long> entity) {
        assertDoesNotThrow(() -> objectMapper.writeValueAsString(entity));
    }

    @ParameterizedTest
    @ValueSource(classes = {Typo.class, Workspace.class})
    void isNotExceptionForToStringWithNewEntity(Class<?> clazz) throws Exception {
        assertDoesNotThrow(clazz.getConstructor().newInstance()::toString);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getEntities")
    void isNotExceptionForToStringWithEntity(final Identifiable<Long> entity) {
        assertDoesNotThrow(entity::toString);
    }

    @ParameterizedTest
    @ValueSource(classes = {Typo.class, Workspace.class})
    void isEqualsIfIdsEquals(Class<Identifiable<Long>> clazz) throws Exception {
        final var entityOne = clazz.getConstructor().newInstance().setId(1L);
        final var entityTwo = clazz.getConstructor().newInstance().setId(1L);
        assertThat(entityOne).isEqualTo(entityTwo).hasSameHashCodeAs(entityTwo);
    }

    @ParameterizedTest
    @ValueSource(classes = {Typo.class, Workspace.class})
    void isNotEqualsIfIdsNotEquals(Class<Identifiable<Long>> clazz) throws Exception {
        final var entityOne = clazz.getConstructor().newInstance().setId(1L);
        final var entityTwo = clazz.getConstructor().newInstance().setId(2L);
        assertThat(entityOne).isNotEqualTo(entityTwo).hasSameHashCodeAs(entityTwo);
    }

    @ParameterizedTest
    @ValueSource(classes = {Typo.class, Workspace.class})
    void isNotEqualsIfOneIdNull(Class<Identifiable<Long>> clazz) throws Exception {
        final var entityOne = clazz.getConstructor().newInstance().setId(1L);
        final var entityTwo = clazz.getConstructor().newInstance();
        assertThat(entityOne).isNotEqualTo(entityTwo).hasSameHashCodeAs(entityTwo);
    }
}
