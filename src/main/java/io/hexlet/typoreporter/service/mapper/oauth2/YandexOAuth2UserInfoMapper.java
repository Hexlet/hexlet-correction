package io.hexlet.typoreporter.service.mapper.oauth2;

import io.hexlet.typoreporter.service.dto.oauth2.OAuth2UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class YandexOAuth2UserInfoMapper {

    @Mapping(target = "email", expression = "java((String) attributes.get(\"default_email\"))")
    @Mapping(target = "nickname", expression = "java((String) attributes.get(\"login\"))")
    @Mapping(target = "firstName", expression = "java((String) attributes.get(\"first_name\"))")
    @Mapping(target = "lastName", expression = "java((String) attributes.get(\"last_name\"))")
    public abstract OAuth2UserInfo toUserInfo(Map<String, Object> attributes);
}
