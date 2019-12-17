package io.hexlet.hexletcorrection.web.rest;

import io.hexlet.hexletcorrection.service.CorrecterService;
import io.hexlet.hexletcorrection.web.rest.errors.BadRequestAlertException;
import io.hexlet.hexletcorrection.service.dto.CorrecterDTO;
import io.hexlet.hexletcorrection.service.dto.CorrecterCriteria;
import io.hexlet.hexletcorrection.service.CorrecterQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.hexlet.hexletcorrection.domain.Correcter}.
 */
@RestController
@RequestMapping("/api")
public class CorrecterResource {

    private final Logger log = LoggerFactory.getLogger(CorrecterResource.class);

    private static final String ENTITY_NAME = "correcter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorrecterService correcterService;

    private final CorrecterQueryService correcterQueryService;

    public CorrecterResource(CorrecterService correcterService, CorrecterQueryService correcterQueryService) {
        this.correcterService = correcterService;
        this.correcterQueryService = correcterQueryService;
    }

    /**
     * {@code POST  /correcters} : Create a new correcter.
     *
     * @param correcterDTO the correcterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new correcterDTO, or with status {@code 400 (Bad Request)} if the correcter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/correcters")
    public ResponseEntity<CorrecterDTO> createCorrecter(@Valid @RequestBody CorrecterDTO correcterDTO) throws URISyntaxException {
        log.debug("REST request to save Correcter : {}", correcterDTO);
        if (correcterDTO.getId() != null) {
            throw new BadRequestAlertException("A new correcter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorrecterDTO result = correcterService.save(correcterDTO);
        return ResponseEntity.created(new URI("/api/correcters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /correcters} : Updates an existing correcter.
     *
     * @param correcterDTO the correcterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correcterDTO,
     * or with status {@code 400 (Bad Request)} if the correcterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the correcterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/correcters")
    public ResponseEntity<CorrecterDTO> updateCorrecter(@Valid @RequestBody CorrecterDTO correcterDTO) throws URISyntaxException {
        log.debug("REST request to update Correcter : {}", correcterDTO);
        if (correcterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CorrecterDTO result = correcterService.save(correcterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correcterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /correcters} : get all the correcters.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of correcters in body.
     */
    @GetMapping("/correcters")
    public ResponseEntity<List<CorrecterDTO>> getAllCorrecters(CorrecterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Correcters by criteria: {}", criteria);
        Page<CorrecterDTO> page = correcterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /correcters/count} : count all the correcters.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/correcters/count")
    public ResponseEntity<Long> countCorrecters(CorrecterCriteria criteria) {
        log.debug("REST request to count Correcters by criteria: {}", criteria);
        return ResponseEntity.ok().body(correcterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /correcters/:id} : get the "id" correcter.
     *
     * @param id the id of the correcterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the correcterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/correcters/{id}")
    public ResponseEntity<CorrecterDTO> getCorrecter(@PathVariable Long id) {
        log.debug("REST request to get Correcter : {}", id);
        Optional<CorrecterDTO> correcterDTO = correcterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(correcterDTO);
    }

    /**
     * {@code DELETE  /correcters/:id} : delete the "id" correcter.
     *
     * @param id the id of the correcterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/correcters/{id}")
    public ResponseEntity<Void> deleteCorrecter(@PathVariable Long id) {
        log.debug("REST request to delete Correcter : {}", id);
        correcterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
