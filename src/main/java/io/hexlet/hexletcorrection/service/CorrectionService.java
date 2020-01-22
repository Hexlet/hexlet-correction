package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.Correction;

import java.util.List;
import java.util.Optional;

public interface CorrectionService {

    Optional<Correction> findById(Long id);

    List<Correction> findByAccountId(Long accountId);

    List<Correction> findByURL(String url);

    List<Correction> findAll();

    Correction create(Correction correction);

    void delete(Long id);
}
