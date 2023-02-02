package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.typo.TypoStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypoRepository extends JpaRepository<Typo, Long> {

    Page<Typo> findPageTypoByWorkspaceName(Pageable pageable, String name);

    Integer deleteTypoById(Long id);

    @Query("""
        select new org.apache.commons.lang3.tuple.ImmutablePair(t.typoStatus, count(t))
        from Typo t
        where t.workspace.name = :wksName
        group by t.typoStatus
        """)
    List<Pair<TypoStatus, Long>> getCountTypoStatusForWorkspaceName(@Param("wksName") String wksName);

    Optional<Typo> getTopByWorkspaceNameOrderByCreatedDate(String wksName);
}
