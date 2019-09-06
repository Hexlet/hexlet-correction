package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CorrectionMessageService {

    Optional<CorrectionMessage> findById(Long id);

    List<CorrectionMessage> findByUsername(String name);

    List<CorrectionMessage> findByURL(String url);

    List<CorrectionMessage> findAll();

    CorrectionMessage save(CorrectionMessage correctionMessage);

    void delete(Long id);
}
