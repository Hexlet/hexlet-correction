package io.hexlet.hexletcorrection.service.mapper;

import io.hexlet.hexletcorrection.domain.*;
import io.hexlet.hexletcorrection.service.dto.CorrectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Correction} and its DTO {@link CorrectionDTO}.
 */
@Mapper(componentModel = "spring", uses = {CorrecterMapper.class})
public interface CorrectionMapper extends EntityMapper<CorrectionDTO, Correction> {

    @Mapping(source = "correcter.id", target = "correcterId")
    @Mapping(source = "resolver.id", target = "resolverId")
    CorrectionDTO toDto(Correction correction);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "removeComments", ignore = true)
    @Mapping(source = "correcterId", target = "correcter")
    @Mapping(source = "resolverId", target = "resolver")
    Correction toEntity(CorrectionDTO correctionDTO);

    default Correction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Correction correction = new Correction();
        correction.setId(id);
        return correction;
    }
}
