package io.hexlet.typoreporter.service.mapper.oauth2;

import io.hexlet.typoreporter.service.dto.oauth2.OAuth2UserInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class GithubOAuth2UserInfoMapper {

    @Mapping(target = "email", expression = "java((String) attributes.get(\"email\"))")
    @Mapping(target = "nickname", expression = "java((String) attributes.get(\"login\"))")
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    public abstract OAuth2UserInfo toUserInfo(Map<String, Object> attributes);

    @AfterMapping
    protected void setName(Map<String, Object> attributes,
                        @MappingTarget OAuth2UserInfo userInfo) {
        String[] parts;
        Object fullName = attributes.get("name");
        if (fullName == null || fullName.toString().isBlank()) {
            parts = new String[] {"", ""};
        }
        parts = fullName.toString().trim().split(" ");
        if (parts.length == 1) {
            parts = new String[]{parts[0], ""};
        }
        userInfo.setFirstName(parts[0]);
        userInfo.setLastName(parts[1]);
    }
}
