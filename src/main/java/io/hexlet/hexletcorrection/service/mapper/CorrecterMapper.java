package io.hexlet.hexletcorrection.service.mapper;

import io.hexlet.hexletcorrection.domain.*;
import io.hexlet.hexletcorrection.service.dto.CorrecterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Correcter} and its DTO {@link CorrecterDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CorrecterMapper extends EntityMapper<CorrecterDTO, Correcter> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CorrecterDTO toDto(Correcter correcter);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "correctionsInProgresses", ignore = true)
    @Mapping(target = "removeCorrectionsInProgress", ignore = true)
    @Mapping(target = "correctionsResolveds", ignore = true)
    @Mapping(target = "removeCorrectionsResolved", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "removeComments", ignore = true)
    Correcter toEntity(CorrecterDTO correcterDTO);

    default Correcter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Correcter correcter = new Correcter();
        correcter.setId(id);
        return correcter;
    }
}
