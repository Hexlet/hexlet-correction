package io.hexlet.typoreporter.service.dto.typo;

import com.fasterxml.jackson.annotation.*;
import io.hexlet.typoreporter.domain.typo.TypoEvent;

import javax.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UpdateTypoEvent(@NotNull Long id, @NotNull TypoEvent typoEvent) {

}
