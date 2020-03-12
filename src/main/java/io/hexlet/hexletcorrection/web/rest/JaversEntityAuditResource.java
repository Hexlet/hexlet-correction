package io.hexlet.hexletcorrection.web.rest;

import io.hexlet.hexletcorrection.domain.EntityAuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.jhipster.web.util.PaginationUtil.generatePaginationHttpHeaders;
import static io.hexlet.hexletcorrection.domain.EntityAuditEvent.fromJaversSnapshot;
import static io.hexlet.hexletcorrection.security.AuthoritiesConstants.ADMIN;
import static java.lang.Class.forName;
import static org.javers.repository.jql.QueryBuilder.byClass;
import static org.javers.repository.jql.QueryBuilder.byInstanceId;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for getting the audit events for entity
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JaversEntityAuditResource {

    private final Javers javers;

    /**
     * fetches all the audited entity types
     *
     * @return
     */
    @GetMapping(value = "/audits/entity/all", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + ADMIN + "\")")
    public List<String> getAuditedEntities() {
        log.debug("REST request to get a page of entities list");
        return List.of("Comment", "Correction", "Preference");
    }

    /**
     * fetches the last 100 change list for an entity class, if limit is passed fetches that many changes
     *
     * @return
     */
    @GetMapping(value = "/audits/entity/changes", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + ADMIN + "\")")
    public ResponseEntity<List<EntityAuditEvent>> getChanges(
        @RequestParam(value = "entityType") String entityType,
        @RequestParam(value = "limit") int limit,
        @RequestParam MultiValueMap<String, String> queryParams,
        UriComponentsBuilder uriBuilder
    ) throws ClassNotFoundException {
        log.debug("REST request to get a page of EntityAuditEvents");
        log.debug("\tEntityType : {}\tLimit : {}\tQuery Params : {}", entityType, limit, queryParams);

        Page<EntityAuditEvent> page = new PageImpl<>(
            javers.findSnapshots(byClass(forName("io.hexlet.hexletcorrection.domain." + entityType))
                .limit(limit)
                .withNewObjectChanges(true)
                .build()
            ).stream()
                .map(fromJaversSnapshot)
                .peek(event -> event.setEntityType(entityType))
                .collect(Collectors.toList())
        );
        HttpHeaders headers = generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    /**
     * fetches a previous version for an entity class and id
     *
     * @return
     */
    @GetMapping(value = "/audits/entity/changes/version/previous", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + ADMIN + "\")")
    public ResponseEntity<EntityAuditEvent> getPrevVersion(
        @RequestParam(value = "qualifiedName") String qualifiedName,
        @RequestParam(value = "entityId") String entityId,
        @RequestParam(value = "commitVersion") Long commitVersion
    ) throws ClassNotFoundException {
        log.debug("REST request to get a page of EntityAuditEvents change");
        log.debug("\tQualified Name : {}\tEntity Id : {}\tCommit Version : {}", qualifiedName, entityId, commitVersion);
        return new ResponseEntity<>(fromJaversSnapshot.apply(javers
            .findSnapshots(byInstanceId(entityId, forName("io.hexlet.hexletcorrection.domain." + qualifiedName))
                .limit(1)
                .withVersion(commitVersion - 1)
                .withNewObjectChanges(true)
                .build()
            ).get(0)), OK);
    }
}
