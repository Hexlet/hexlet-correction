package io.hexlet.hexletcorrection.repository;

import io.hexlet.hexletcorrection.domain.Correction;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Correction entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface CorrectionRepository extends JpaRepository<Correction, Long>, JpaSpecificationExecutor<Correction> {

}
