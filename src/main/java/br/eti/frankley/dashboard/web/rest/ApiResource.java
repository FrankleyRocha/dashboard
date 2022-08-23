package br.eti.frankley.dashboard.web.rest;

import br.eti.frankley.dashboard.domain.Api;
import br.eti.frankley.dashboard.repository.ApiRepository;
import br.eti.frankley.dashboard.service.ApiService;
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
 * REST controller for managing {@link br.eti.frankley.dashboard.domain.Api}.
 */
@RestController
@RequestMapping("/api")
public class ApiResource {

    private final Logger log = LoggerFactory.getLogger(ApiResource.class);

    private static final String ENTITY_NAME = "api";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiService apiService;

    private final ApiRepository apiRepository;

    public ApiResource(ApiService apiService, ApiRepository apiRepository) {
        this.apiService = apiService;
        this.apiRepository = apiRepository;
    }

    /**
     * {@code POST  /apis} : Create a new api.
     *
     * @param api the api to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new api, or with status {@code 400 (Bad Request)} if the api has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apis")
    public ResponseEntity<Api> createApi(@Valid @RequestBody Api api) throws URISyntaxException {
        log.debug("REST request to save Api : {}", api);
        if (api.getId() != null) {
            throw new BadRequestAlertException("A new api cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Api result = apiService.save(api);
        return ResponseEntity
            .created(new URI("/api/apis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apis/:id} : Updates an existing api.
     *
     * @param id the id of the api to save.
     * @param api the api to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated api,
     * or with status {@code 400 (Bad Request)} if the api is not valid,
     * or with status {@code 500 (Internal Server Error)} if the api couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apis/{id}")
    public ResponseEntity<Api> updateApi(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Api api)
        throws URISyntaxException {
        log.debug("REST request to update Api : {}, {}", id, api);
        if (api.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, api.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Api result = apiService.update(api);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, api.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apis/:id} : Partial updates given fields of an existing api, field will ignore if it is null
     *
     * @param id the id of the api to save.
     * @param api the api to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated api,
     * or with status {@code 400 (Bad Request)} if the api is not valid,
     * or with status {@code 404 (Not Found)} if the api is not found,
     * or with status {@code 500 (Internal Server Error)} if the api couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Api> partialUpdateApi(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Api api)
        throws URISyntaxException {
        log.debug("REST request to partial update Api partially : {}, {}", id, api);
        if (api.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, api.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Api> result = apiService.partialUpdate(api);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, api.getId().toString())
        );
    }

    /**
     * {@code GET  /apis} : get all the apis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apis in body.
     */
    @GetMapping("/apis")
    public ResponseEntity<List<Api>> getAllApis(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Apis");
        Page<Api> page = apiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apis/:id} : get the "id" api.
     *
     * @param id the id of the api to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the api, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apis/{id}")
    public ResponseEntity<Api> getApi(@PathVariable Long id) {
        log.debug("REST request to get Api : {}", id);
        Optional<Api> api = apiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(api);
    }

    /**
     * {@code DELETE  /apis/:id} : delete the "id" api.
     *
     * @param id the id of the api to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apis/{id}")
    public ResponseEntity<Void> deleteApi(@PathVariable Long id) {
        log.debug("REST request to delete Api : {}", id);
        apiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
