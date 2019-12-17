package io.hexlet.hexletcorrection.repository;
import io.hexlet.hexletcorrection.domain.Correcter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Correcter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrecterRepository extends JpaRepository<Correcter, Long>, JpaSpecificationExecutor<Correcter> {

}
