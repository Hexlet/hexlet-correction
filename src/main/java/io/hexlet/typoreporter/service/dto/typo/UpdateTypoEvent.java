package io.hexlet.typoreporter.service.dto.typo;

import io.hexlet.typoreporter.domain.typo.TypoEvent;

import javax.validation.constraints.NotNull;

public record UpdateTypoEvent(@NotNull Long id, @NotNull TypoEvent typoEvent) {

}
