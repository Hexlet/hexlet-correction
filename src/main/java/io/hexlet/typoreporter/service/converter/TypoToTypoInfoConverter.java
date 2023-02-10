package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.mapper.TypoMapper;
import io.hexlet.typoreporter.service.mapper.TypoMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TypoToTypoInfoConverter implements Converter<Typo, TypoInfo> {

    /**
     * Convert the source object of type {@code Typo} to target type {@code TypoInfo}.
     *
     * @param source the source object to convert, which must be an instance of {@code Typo} (never {@code null})
     * @return the converted object, which must be an instance of {@code TypoInfo} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */

    private final TypoMapper typoMapper = new TypoMapperImpl();
    @Override
    public TypoInfo convert(Typo source) {
        return typoMapper.toTypoInfo(source);
    }
}
