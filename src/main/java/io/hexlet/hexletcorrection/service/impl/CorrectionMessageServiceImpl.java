package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.repository.CorrectionMessageRepository;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorrectionMessageServiceImpl implements CorrectionMessageService {

    private final CorrectionMessageRepository correctionMessageRepository;

    @Override
    public Optional<CorrectionMessage> findById(Long id) {
        return correctionMessageRepository.findById(id);
    }

    @Override
    public List<CorrectionMessage> findByUsername(String username) {
        return correctionMessageRepository.findCorrectionMessageByUsernameContainingIgnoreCase(username);
    }

    @Override
    public List<CorrectionMessage> findByURL(String url) {
        return correctionMessageRepository.findCorrectionMessageByPageURL(url);
    }

    @Override
    public List<CorrectionMessage> findAll() {
        return correctionMessageRepository.findAll();
    }

    @Override
    public CorrectionMessage save(CorrectionMessage correctionMessage) {
        return correctionMessageRepository.save(correctionMessage);
    }

    @Override
    public void delete(Long id) {
        correctionMessageRepository.deleteById(id);
    }
}
