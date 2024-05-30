package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.workspace.AllowedUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllowedUrlRepository extends JpaRepository<AllowedUrl, Long> {
    Optional<AllowedUrl> findAllowedUrlByUrlAndWorkspaceId(String url, Long wksId);
    List<AllowedUrl> findByWorkspaceId(Long wksId);
    Page<AllowedUrl> findPageAllowedUrlByWorkspaceId(Pageable pageable, Long wksId);
}
