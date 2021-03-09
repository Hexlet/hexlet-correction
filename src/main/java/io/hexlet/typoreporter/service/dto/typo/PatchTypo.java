package io.hexlet.typoreporter.service.dto.typo;

import com.fasterxml.jackson.annotation.*;
import io.hexlet.typoreporter.domain.typo.constraint.ReporterComment;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record PatchTypo(@ReporterComment String reporterComment) {

    @JsonCreator
    public PatchTypo(String reporterComment) {
        this.reporterComment = reporterComment;
    }
}
