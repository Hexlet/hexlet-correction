package io.hexlet.hexletcorrection.repository;

import io.hexlet.hexletcorrection.domain.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Preference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long>, JpaSpecificationExecutor<Preference> {

}
