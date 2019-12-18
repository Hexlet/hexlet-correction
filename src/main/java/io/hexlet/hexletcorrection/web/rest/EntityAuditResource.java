package io.hexlet.hexletcorrection.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import io.hexlet.hexletcorrection.domain.EntityAuditEvent;
import io.hexlet.hexletcorrection.repository.EntityAuditEventRepository;
import io.hexlet.hexletcorrection.security.AuthoritiesConstants;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for getting the audit events for entity
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EntityAuditResource {

    private final Logger log = LoggerFactory.getLogger(EntityAuditResource.class);

    private final EntityAuditEventRepository entityAuditEventRepository;

    public EntityAuditResource(EntityAuditEventRepository entityAuditEventRepository) {
        this.entityAuditEventRepository = entityAuditEventRepository;
    }

    /**
     * fetches the last 100 change list for an entity class, if limit is passed fetches that many changes
     *
     * @return
     */
    @RequestMapping(value = "/audits/entity/changes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<EntityAuditEvent>> getChanges(@RequestParam(value = "entityType") String entityType,
                                                             @RequestParam(value = "entityId", required = false) Long entityId,
                                                             Pageable pageable) {
        log.debug("REST request to get a page of EntityAuditEvents");
        Page<EntityAuditEvent> page = entityId != null ?
            entityAuditEventRepository.findAllByEntityTypeAndEntityId(entityType, entityId, pageable) :
            entityAuditEventRepository.findAllByEntityType(entityType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromUriString("/api/audits/entity/changes") ,page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * fetches a previous version for for an entity class and id
     *
     * @return
     */
    @RequestMapping(value = "/audits/entity/changes/version/previous",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<EntityAuditEvent> getPrevVersion(@RequestParam(value = "qualifiedName") String qualifiedName,
                                                           @RequestParam(value = "entityId") Long entityId,
                                                           @RequestParam(value = "commitVersion") Integer commitVersion)
        throws URISyntaxException {
        EntityAuditEvent prev = entityAuditEventRepository.findOneByEntityTypeAndEntityIdAndCommitVersion(qualifiedName, entityId, commitVersion);
        return new ResponseEntity<>(prev, HttpStatus.OK);
    }

}
