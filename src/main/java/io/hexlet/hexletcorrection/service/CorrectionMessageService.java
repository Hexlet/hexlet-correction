package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;

import java.util.List;
import java.util.Optional;

public interface CorrectionMessageService {

    Optional<CorrectionMessage> findById(Long id);

    List<CorrectionMessage> findByURL(String url);

    List<CorrectionMessage> findAll();

    CorrectionMessage create(CorrectionMessage correctionMessage);

    void delete(Long id);
}
