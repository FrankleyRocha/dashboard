package br.eti.frankley.dashboard.web.rest;

import br.eti.frankley.dashboard.domain.Conexao;
import br.eti.frankley.dashboard.repository.ConexaoRepository;
import br.eti.frankley.dashboard.service.ConexaoService;
import br.eti.frankley.dashboard.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.eti.frankley.dashboard.domain.Conexao}.
 */
@RestController
@RequestMapping("/api")
public class ConexaoResource {

    private final Logger log = LoggerFactory.getLogger(ConexaoResource.class);

    private static final String ENTITY_NAME = "conexao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConexaoService conexaoService;

    private final ConexaoRepository conexaoRepository;

    public ConexaoResource(ConexaoService conexaoService, ConexaoRepository conexaoRepository) {
        this.conexaoService = conexaoService;
        this.conexaoRepository = conexaoRepository;
    }

    /**
     * {@code POST  /conexaos} : Create a new conexao.
     *
     * @param conexao the conexao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conexao, or with status {@code 400 (Bad Request)} if the conexao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conexaos")
    public ResponseEntity<Conexao> createConexao(@Valid @RequestBody Conexao conexao) throws URISyntaxException {
        log.debug("REST request to save Conexao : {}", conexao);
        if (conexao.getId() != null) {
            throw new BadRequestAlertException("A new conexao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conexao result = conexaoService.save(conexao);
        return ResponseEntity
            .created(new URI("/api/conexaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conexaos/:id} : Updates an existing conexao.
     *
     * @param id the id of the conexao to save.
     * @param conexao the conexao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conexao,
     * or with status {@code 400 (Bad Request)} if the conexao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conexao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conexaos/{id}")
    public ResponseEntity<Conexao> updateConexao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Conexao conexao
    ) throws URISyntaxException {
        log.debug("REST request to update Conexao : {}, {}", id, conexao);
        if (conexao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conexao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conexaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conexao result = conexaoService.update(conexao);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conexao.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conexaos/:id} : Partial updates given fields of an existing conexao, field will ignore if it is null
     *
     * @param id the id of the conexao to save.
     * @param conexao the conexao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conexao,
     * or with status {@code 400 (Bad Request)} if the conexao is not valid,
     * or with status {@code 404 (Not Found)} if the conexao is not found,
     * or with status {@code 500 (Internal Server Error)} if the conexao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conexaos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Conexao> partialUpdateConexao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Conexao conexao
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conexao partially : {}, {}", id, conexao);
        if (conexao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conexao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conexaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conexao> result = conexaoService.partialUpdate(conexao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conexao.getId().toString())
        );
    }

    /**
     * {@code GET  /conexaos} : get all the conexaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conexaos in body.
     */
    @GetMapping("/conexaos")
    public ResponseEntity<List<Conexao>> getAllConexaos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Conexaos");
        Page<Conexao> page = conexaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conexaos/:id} : get the "id" conexao.
     *
     * @param id the id of the conexao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conexao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conexaos/{id}")
    public ResponseEntity<Conexao> getConexao(@PathVariable Long id) {
        log.debug("REST request to get Conexao : {}", id);
        Optional<Conexao> conexao = conexaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conexao);
    }

    /**
     * {@code DELETE  /conexaos/:id} : delete the "id" conexao.
     *
     * @param id the id of the conexao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conexaos/{id}")
    public ResponseEntity<Void> deleteConexao(@PathVariable Long id) {
        log.debug("REST request to delete Conexao : {}", id);
        conexaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
