package io.hexlet.typoreporter.service.dto.typo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.hexlet.typoreporter.domain.typo.TypoEvent;
import io.hexlet.typoreporter.domain.typo.constraint.ReporterComment;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record PatchTypo(@ReporterComment String reporterComment, TypoEvent typoEvent) {

}
