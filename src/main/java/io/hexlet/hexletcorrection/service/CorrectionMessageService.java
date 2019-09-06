package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CorrectionMessageService {

    Mono<CorrectionMessage> findById(String id);

    Flux<CorrectionMessage> findByUsername(String name);

    Flux<CorrectionMessage> findByURL(String url);

    Flux<CorrectionMessage> findAll();

    Mono<CorrectionMessage> save(CorrectionMessage correctionMessage);

    Mono<Void> delete(String id);
}
