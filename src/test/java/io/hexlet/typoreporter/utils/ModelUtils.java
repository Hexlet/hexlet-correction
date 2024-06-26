package io.hexlet.typoreporter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

@Component
public class ModelUtils {
    @Autowired
    private ObjectMapper objectMapper;

    public MultiValueMap<String, String> toFormParams(Object dto) throws Exception {
        return toFormParams(dto, Set.of());
    }

    public MultiValueMap<String, String> toFormParams(Object dto, Set<String> excludeFields) throws Exception {
        ObjectReader reader = objectMapper.readerFor(Map.class);
        Map<String, String> map = reader.readValue(objectMapper.writeValueAsString(dto));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        map.entrySet().stream()
            .filter(e -> !excludeFields.contains(e.getKey()))
            .forEach(e -> multiValueMap.add(e.getKey(), (e.getValue() == null ? "" : e.getValue())));
        return multiValueMap;
    }
}
