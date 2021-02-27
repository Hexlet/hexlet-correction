package io.hexlet.typoreporter.domain.typo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
class TypoTest {

    @Autowired
    private ObjectMapper objectMapper;

    private static Stream<String> getValidTyposJson() {
        final var jsonEmptyValues = """
                {
                  "id": "",
                  "pageUrl": "",
                  "reporterName": "",
                  "reporterComment": "",
                  "textBeforeTypo": "",
                  "textTypo": "",
                  "textAfterTypo": "",
                  "typoStatus": "REPORTED"
                }
                """;
        final var jsonNullValues = """
                {
                  "id": null,
                  "pageUrl": null,
                  "reporterName": null,
                  "reporterComment": null,
                  "textBeforeTypo": null,
                  "textTypo": null,
                  "textAfterTypo": null,
                  "typoStatus": null
                }
                """;
        final var typoJson = """
                {
                  "id": 1,
                  "pageUrl": "http://site.com",
                  "reporterName": "reporterName",
                  "reporterComment": "reporterComment",
                  "textBeforeTypo": "textBeforeTypo",
                  "textTypo": "textTypo",
                  "textAfterTypo": "textAfterTypo",
                  "typoStatus": "%s"
                }
                """;
        final var typoJsonStream = Arrays.stream(TypoStatus.values()).map(typoJson::formatted);
        return Stream.concat(typoJsonStream, Stream.of("{}", jsonEmptyValues, jsonNullValues));
    }

    private static Stream<String> getInvalidTyposJson() {
        final var jsonEmptyValues = """
                {
                  "id": "",
                  "pageUrl": "",
                  "reporterName": "",
                  "reporterComment": "",
                  "textBeforeTypo": "",
                  "textTypo": "",
                  "textAfterTypo": "",
                  "typoStatus": ""
                }
                """;
        return Stream.of(jsonEmptyValues, "notJson");
    }

    @ParameterizedTest
    @MethodSource("getValidTyposJson")
    void isDeserializeValidJsonToEntity(final String json) {
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, Typo.class));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("getInvalidTyposJson")
    void isNotDeserializeInvalidJsonToEntity(final String json) {
        assertThrows(Exception.class, () -> objectMapper.readValue(json, Typo.class));
    }
}
