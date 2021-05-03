package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.typo.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TypoRepository extends JpaRepository<Typo, Long> {

    Page<Typo> findPageTypoByWorkspaceName(Pageable pageable, String name);

    Integer deleteTypoById(Long id);

    //TODO add tests
    @Query("""
        select new org.apache.commons.lang3.tuple.ImmutablePair(t.typoStatus, count(t))
        from Typo t
        where t.workspace.name = :wksName
        group by t.typoStatus
        """)
    List<Pair<TypoStatus, Long>> getCountTypoStatusForWorkspaceName(String wksName);

    //TODO add tests
    Optional<Typo> getTopByWorkspaceNameOrderByCreatedDate(String wksName);
}
