package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.hexlet.typoreporter.test.asserts.TypoInfoAssert.assertThat;

@SpringBootTest(classes = {TypoMapperImpl.class})
class TypoToTypoInfoMapperTest {
    @Autowired
    private TypoMapper typoMapper;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void requestToTypoInfo(final Typo typo) {
        final var typoInfo = typoMapper.toTypoInfo(typo);
        assertThat(typoInfo).isEqualsToTypoInfo(typo);
    }
}
