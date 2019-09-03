package io.hexlet.hexletcorrection.services.impl;

import io.hexlet.hexletcorrection.persistence.entities.CorrectionMessage;
import io.hexlet.hexletcorrection.persistence.repository.CorrectionMessageRepository;
import io.hexlet.hexletcorrection.services.CorrectionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CorrectionMessageServiceImpl implements CorrectionMessageService {
    
    private final CorrectionMessageRepository correctionMessageRepository;
    
    @Override
    public Set<CorrectionMessage> findAll() {
        return new HashSet<>(correctionMessageRepository.findAll());
    }
    
    @Override
    public Set<CorrectionMessage> findByName(String userName) {
        return new HashSet<>(correctionMessageRepository.findCorrectionMessageByUserNameContainingIgnoreCase(userName));
    }
    
    @Override
    public CorrectionMessage findById(Long id) {
        return correctionMessageRepository.findById(id).orElse(null);
    }
    
    @Override
    public CorrectionMessage save(CorrectionMessage entity) {
        return correctionMessageRepository.save(entity);
    }
    
    @Override
    public void delete(Long id) {
        correctionMessageRepository.deleteById(id);
    }
}
