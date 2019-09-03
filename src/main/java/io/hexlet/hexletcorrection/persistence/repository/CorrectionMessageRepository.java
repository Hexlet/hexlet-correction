package io.hexlet.hexletcorrection.persistence.repository;

import io.hexlet.hexletcorrection.persistence.entities.CorrectionMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CorrectionMessageRepository extends JpaRepository<CorrectionMessage, Long> {
    Set<CorrectionMessage> findCorrectionMessageByUserNameContainingIgnoreCase(String userName);
}
