package io.hexlet.hexletcorrection.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.hexlet.hexletcorrection.service.CorrectionQueryService;
import io.hexlet.hexletcorrection.service.CorrectionService;
import io.hexlet.hexletcorrection.service.dto.CorrectionCriteria;
import io.hexlet.hexletcorrection.service.dto.CorrectionDTO;
import io.hexlet.hexletcorrection.web.rest.errors.BadRequestAlertException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus.REPORTED;

/**
 * REST controller for managing {@link io.hexlet.hexletcorrection.domain.Correction}.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CorrectionResource {

    private static final String ENTITY_NAME = "correction";

    private final CorrectionService correctionService;

    private final CorrectionQueryService correctionQueryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /corrections} : Create a new correction.
     *
     * @param correctionDTO the correctionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new correctionDTO, or with status {@code 400 (Bad Request)} if the correction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/corrections")
    public ResponseEntity<CorrectionDTO> createCorrection(@Valid @RequestBody CorrectionDTO correctionDTO) throws URISyntaxException {
        log.debug("REST request to save Correction : {}", correctionDTO);
        if (correctionDTO.getId() != null) {
            throw new BadRequestAlertException("A new correction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        correctionDTO.setCorrectionStatus(REPORTED);
        CorrectionDTO result = correctionService.save(correctionDTO);
        return ResponseEntity.created(new URI("/api/corrections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /corrections} : Updates an existing correction.
     *
     * @param correctionDTO the correctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correctionDTO,
     * or with status {@code 400 (Bad Request)} if the correctionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the correctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/corrections")
    public ResponseEntity<CorrectionDTO> updateCorrection(@Valid @RequestBody CorrectionDTO correctionDTO) throws URISyntaxException {
        log.debug("REST request to update Correction : {}", correctionDTO);
        if (correctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CorrectionDTO result = correctionService.save(correctionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correctionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /corrections} : get all the corrections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corrections in body.
     */
    @GetMapping("/corrections")
    public ResponseEntity<List<CorrectionDTO>> getAllCorrections(CorrectionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Corrections by criteria: {}", criteria);
        Page<CorrectionDTO> page = correctionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /corrections/count} : count all the corrections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/corrections/count")
    public ResponseEntity<Long> countCorrections(CorrectionCriteria criteria) {
        log.debug("REST request to count Corrections by criteria: {}", criteria);
        return ResponseEntity.ok().body(correctionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /corrections/:id} : get the "id" correction.
     *
     * @param id the id of the correctionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the correctionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/corrections/{id}")
    public ResponseEntity<CorrectionDTO> getCorrection(@PathVariable Long id) {
        log.debug("REST request to get Correction : {}", id);
        Optional<CorrectionDTO> correctionDTO = correctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(correctionDTO);
    }

    /**
     * {@code DELETE  /corrections/:id} : delete the "id" correction.
     *
     * @param id the id of the correctionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/corrections/{id}")
    public ResponseEntity<Void> deleteCorrection(@PathVariable Long id) {
        log.debug("REST request to delete Correction : {}", id);
        correctionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
