package br.eti.frankley.dashboard.service;

import br.eti.frankley.dashboard.domain.Api;
import br.eti.frankley.dashboard.repository.ApiRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Api}.
 */
@Service
@Transactional
public class ApiService {

    private final Logger log = LoggerFactory.getLogger(ApiService.class);

    private final ApiRepository apiRepository;

    public ApiService(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    /**
     * Save a api.
     *
     * @param api the entity to save.
     * @return the persisted entity.
     */
    public Api save(Api api) {
        log.debug("Request to save Api : {}", api);
        return apiRepository.save(api);
    }

    /**
     * Update a api.
     *
     * @param api the entity to save.
     * @return the persisted entity.
     */
    public Api update(Api api) {
        log.debug("Request to save Api : {}", api);
        return apiRepository.save(api);
    }

    /**
     * Partially update a api.
     *
     * @param api the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Api> partialUpdate(Api api) {
        log.debug("Request to partially update Api : {}", api);

        return apiRepository
            .findById(api.getId())
            .map(existingApi -> {
                if (api.getHash() != null) {
                    existingApi.setHash(api.getHash());
                }
                if (api.getUrlBase() != null) {
                    existingApi.setUrlBase(api.getUrlBase());
                }
                if (api.getToken() != null) {
                    existingApi.setToken(api.getToken());
                }

                return existingApi;
            })
            .map(apiRepository::save);
    }

    /**
     * Get all the apis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Api> findAll(Pageable pageable) {
        log.debug("Request to get all Apis");
        return apiRepository.findAll(pageable);
    }

    /**
     * Get one api by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Api> findOne(Long id) {
        log.debug("Request to get Api : {}", id);
        return apiRepository.findById(id);
    }

    /**
     * Delete the api by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Api : {}", id);
        apiRepository.deleteById(id);
    }
}
