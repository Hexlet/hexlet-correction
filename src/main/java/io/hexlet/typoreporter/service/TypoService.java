package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.typo.*;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.service.dto.typo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

@Service
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
    public Optional<Typo> patchTypoById(Long id, PatchTypo patch) {
        return repository.findById(id)
                .map(typo -> typo.setReporterComment(patch.reporterComment()))
                .map(updateStatus(patch.typoEvent()))
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
