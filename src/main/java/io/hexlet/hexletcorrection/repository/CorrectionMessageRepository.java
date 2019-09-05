package io.hexlet.hexletcorrection.repository;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CorrectionMessageRepository extends ReactiveMongoRepository<CorrectionMessage, String> {
    Flux<CorrectionMessage> findCorrectionMessageByUsernameContainingIgnoreCase(String username);
    
    Flux<CorrectionMessage> findCorrectionMessageByPageURL(String pageURL);
}