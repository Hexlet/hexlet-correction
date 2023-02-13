package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import static io.hexlet.typoreporter.test.asserts.TypoInfoAssert.assertThat;

class TypoToTypoInfoMapperTest {

    private final TypoMapper typoMapper = Mappers.getMapper(TypoMapper.class);

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void requestToTypoInfo(final Typo typo) {
        final var typoInfo = typoMapper.toTypoInfo(typo);
        assertThat(typoInfo).isEqualsToTypoInfo(typo);
    }
}
