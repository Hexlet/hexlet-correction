package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.typo.Typo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypoRepository extends JpaRepository<Typo, Long> {

    Integer deleteTypoById(Long id);
}
