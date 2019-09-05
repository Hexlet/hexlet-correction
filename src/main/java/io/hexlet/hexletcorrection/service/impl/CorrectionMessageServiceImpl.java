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
    
    private final CorrectionMessageRepository corrMsgRep;
    
    @Override
    public Mono<CorrectionMessage> findById(String id) {
        return corrMsgRep.findById(id);
    }
    
    @Override
    public Flux<CorrectionMessage> findByUsername(String username) {
        return corrMsgRep.findCorrectionMessageByUsernameContainingIgnoreCase(username);
    }
    
    @Override
    public Flux<CorrectionMessage> findByURL(String url) {
        return corrMsgRep.findCorrectionMessageByPageURL(url);
    }
    
    @Override
    public Flux<CorrectionMessage> findAll() {
        return corrMsgRep.findAll();
    }
    
    @Override
    public Mono<CorrectionMessage> save(CorrectionMessage correctionMessage) {
        return corrMsgRep.save(correctionMessage);
    }
    
    @Override
    public Mono<CorrectionMessage> delete(String id) {
        Mono<CorrectionMessage> deletedMsg = corrMsgRep.findById(id);
        corrMsgRep.deleteById(id);
        return deletedMsg;
    }
}