package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.typo.*;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.service.dto.typo.*;
import lombok.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

@Service
@Validated
@RequiredArgsConstructor
public class TypoService {

    private final TypoRepository repository;

    private final ConversionService conversionService;

    public ReportedTypo addTypoReport(final TypoReport report) {
        final var typo = requireNonNull(conversionService.convert(report, Typo.class));
        return conversionService.convert(repository.save(typo), ReportedTypo.class);
    }

    @Transactional(readOnly = true)
    public Page<Typo> getTypoPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Typo> getTypoById(final Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Optional<Typo> patchTypoById(Long id, PatchTypo patchTypo) {
        return repository.findById(id)
            .map(typo -> typo.setReporterComment(patchTypo.reporterComment()))
            .map(repository::save);
    }

    @Transactional
    public Optional<Typo> updateTypoStatusById(Long id, TypoEvent event) {
        return repository.findById(id)
            .map(updateStatus(event))
            .map(repository::save);
    }

    @Transactional
    public Integer deleteTypoById(Long id) {
        return repository.deleteTypoById(id);
    }

    private UnaryOperator<Typo> updateStatus(TypoEvent typoEvent) {
        final var event = Optional.ofNullable(typoEvent);
        return typo -> typo.setTypoStatus(event.map(typo.getTypoStatus()::next).orElseGet(typo::getTypoStatus));
    }
}
