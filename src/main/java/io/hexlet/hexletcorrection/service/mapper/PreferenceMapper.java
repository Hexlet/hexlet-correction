package io.hexlet.hexletcorrection.service.mapper;


import io.hexlet.hexletcorrection.domain.Preference;
import io.hexlet.hexletcorrection.service.dto.PreferenceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Preference} and its DTO {@link PreferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PreferenceMapper extends EntityMapper<PreferenceDTO, Preference> {

    @Mapping(source = "user.id", target = "userId")
    PreferenceDTO toDto(Preference preference);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "correctionsInProgresses", ignore = true)
    @Mapping(target = "resolvedCorrections", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Preference toEntity(PreferenceDTO preferenceDTO);

    default Preference fromId(Long id) {
        if (id == null) {
            return null;
        }
        Preference preference = new Preference();
        preference.setId(id);
        return preference;
    }
}
