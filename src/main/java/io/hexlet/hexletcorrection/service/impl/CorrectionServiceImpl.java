package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.repository.CorrectionRepository;
import io.hexlet.hexletcorrection.service.CorrectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CorrectionServiceImpl implements CorrectionService {

    private final CorrectionRepository correctionRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Correction> findById(Long id) {
        return correctionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Correction> findByAccountId(Long accountId) {
        return correctionRepository.findByAccountId(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Correction> findByURL(String url) {
        return correctionRepository.findByPageURL(url);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Correction> findAll() {
        return correctionRepository.findAll();
    }

    @Override
    public Correction create(Correction correction) {
        return correctionRepository.save(correction);
    }

    @Override
    public void delete(Long id) {
        correctionRepository.deleteById(id);
    }
}
