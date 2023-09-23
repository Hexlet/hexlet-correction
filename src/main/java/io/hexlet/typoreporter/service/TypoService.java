package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.typo.TypoEvent;
import io.hexlet.typoreporter.domain.typo.TypoStatus;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import io.hexlet.typoreporter.service.mapper.TypoMapper;
import io.hexlet.typoreporter.web.exception.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static java.util.Map.Entry.comparingByKey;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class TypoService {

    private final WorkspaceRepository workspaceRepository;

    private final TypoRepository repository;

    private final TypoMapper typoMapper;

    @Setter(onMethod_ = {@Autowired})
    private WorkspaceService workspaceService;

    @Transactional
    public ReportedTypo addTypoReport(final TypoReport report, final String wksIdStr) {
        final var workspace = workspaceService.getWorkspaceByName(wksIdStr)
            .orElseThrow(() -> new WorkspaceNotFoundException(wksIdStr));

        final var typo = requireNonNull(typoMapper.toTypo(report));
        workspace.addTypo(typo);
        return typoMapper.toReportedTypo(repository.save(typo));
    }

    @Transactional
    public ReportedTypo addTypoReport(final TypoReport report, final Long wksId) {
        final var wks = workspaceRepository.getReferenceById(wksId);

        final var typo = requireNonNull(typoMapper.toTypo(report));
        typo.setWorkspace(wks);
        return typoMapper.toReportedTypo(repository.save(typo));
    }

    @Transactional(readOnly = true)
    public Page<TypoInfo> getTypoPage(final Pageable pageable, final String wksName) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            throw new WorkspaceNotFoundException(wksName);
        }
        return repository.findPageTypoByWorkspaceName(pageable, wksName)
            .map(typoMapper::toTypoInfo);
    }

    @Transactional(readOnly = true)
    public Page<TypoInfo> getTypoPageFiltered(final Pageable pageable, final String wksName, final String typoStatus) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            throw new WorkspaceNotFoundException(wksName);
        }

        TypoStatus typoStatusEnum;
        try {
            typoStatusEnum = TypoStatus.valueOf(typoStatus);
        } catch (IllegalArgumentException e) {
            typoStatusEnum = null;
        }

        return repository.findPageTypoByWorkspaceNameAndTypoStatus(pageable, wksName, typoStatusEnum)
            .map(typoMapper::toTypoInfo);
    }

    @Transactional(readOnly = true)
    public List<Pair<TypoStatus, Long>> getCountTypoByStatusForWorkspaceName(final String wksName) {
        final var countTypoByStatus = repository.getCountTypoStatusForWorkspaceName(wksName)
            .stream()
            .collect(toMap(Pair::getKey, Pair::getValue));
        Arrays.stream(TypoStatus.values()).forEach(ts -> countTypoByStatus.putIfAbsent(ts, 0L));

        return countTypoByStatus.entrySet()
            .stream()
            .sorted(comparingByKey())
            .map(Pair::of)
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<TypoInfo> getLastTypoByWorkspaceName(final String wksName) {
        return repository.getTopByWorkspaceNameOrderByCreatedDate(wksName)
            .map(typoMapper::toTypoInfo);
    }

    @Transactional
    public Optional<TypoInfo> updateTypoStatus(final Long id, final TypoEvent event) {
        return repository.findById(id)
            .map(updateStatus(event))
            .map(repository::save)
            .map(typoMapper::toTypoInfo);
    }

    @Transactional
    public Integer deleteTypoById(Long id) {
        return repository.deleteTypoById(id);
    }

    private UnaryOperator<Typo> updateStatus(TypoEvent typoEvent) {
        return typo -> typo.setTypoStatus(
            ofNullable(typoEvent)
                .map(typo.getTypoStatus()::next)
                .orElseGet(typo::getTypoStatus)
        );
    }
}
