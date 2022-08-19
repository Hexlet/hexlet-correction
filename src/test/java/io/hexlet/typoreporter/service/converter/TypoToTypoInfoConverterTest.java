package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.converter.Converter;

import static io.hexlet.typoreporter.test.asserts.TypoInfoAssert.assertThat;

public class TypoToTypoInfoConverterTest {
    private final Converter<Typo, TypoInfo> converter = new TypoToTypoInfoConverter();

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void requestToTypoInfo(final Typo typo) {
        final var typoInfo = converter.convert(typo);
        assertThat(typoInfo).isEqualsToTypoInfo(typo);
    }
}
