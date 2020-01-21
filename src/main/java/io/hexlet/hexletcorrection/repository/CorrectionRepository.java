package io.hexlet.hexletcorrection.repository;

import io.hexlet.hexletcorrection.domain.Correction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrectionRepository extends JpaRepository<Correction, Long> {

    List<Correction> findByPageURL(String pageURL);

    List<Correction> findByAccountId(Long accountId);
}
