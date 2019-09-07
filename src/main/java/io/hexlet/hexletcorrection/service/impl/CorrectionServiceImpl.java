package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.repository.CorrectionRepository;
import io.hexlet.hexletcorrection.service.CorrectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorrectionServiceImpl implements CorrectionService {

    private final CorrectionRepository correctionRepository;

    @Override
    public Optional<Correction> findById(Long id) {
        return correctionRepository.findById(id);
    }

    @Override
    public List<Correction> findByURL(String url) {
        return correctionRepository.findByPageURL(url);
    }

    @Override
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
