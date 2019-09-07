package io.hexlet.hexletcorrection.repository;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrectionMessageRepository extends JpaRepository<CorrectionMessage, Long> {

    List<CorrectionMessage> findByPageURL(String pageURL);
}
