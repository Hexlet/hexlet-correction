package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.repository.CorrectionMessageRepository;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CorrectionMessageServiceImpl implements CorrectionMessageService {

    private final CorrectionMessageRepository correctionMessageRepository;

    @Override
    public Mono<CorrectionMessage> findById(String id) {
        return correctionMessageRepository.findById(id);
    }

    @Override
    public Flux<CorrectionMessage> findByUsername(String username) {
        return correctionMessageRepository.findCorrectionMessageByUsernameContainingIgnoreCase(username);
    }

    @Override
    public Flux<CorrectionMessage> findByURL(String url) {
        return correctionMessageRepository.findCorrectionMessageByPageURL(url);
    }

    @Override
    public Flux<CorrectionMessage> findAll() {
        return correctionMessageRepository.findAll();
    }

    @Override
    public Mono<CorrectionMessage> save(CorrectionMessage correctionMessage) {
        return correctionMessageRepository.save(correctionMessage);
    }

    @Override
    public Mono<Void> delete(String id) {
        return correctionMessageRepository.deleteById(id);
    }
}
