package br.eti.frankley.dashboard.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.eti.frankley.dashboard.IntegrationTest;
import br.eti.frankley.dashboard.domain.Api;
import br.eti.frankley.dashboard.repository.ApiRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApiResourceIT {

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_URL_BASE = "AAAAAAAAAA";
    private static final String UPDATED_URL_BASE = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApiMockMvc;

    private Api api;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Api createEntity(EntityManager em) {
        Api api = new Api().hash(DEFAULT_HASH).urlBase(DEFAULT_URL_BASE).token(DEFAULT_TOKEN);
        return api;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Api createUpdatedEntity(EntityManager em) {
        Api api = new Api().hash(UPDATED_HASH).urlBase(UPDATED_URL_BASE).token(UPDATED_TOKEN);
        return api;
    }

    @BeforeEach
    public void initTest() {
        api = createEntity(em);
    }

    @Test
    @Transactional
    void createApi() throws Exception {
        int databaseSizeBeforeCreate = apiRepository.findAll().size();
        // Create the Api
        restApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isCreated());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeCreate + 1);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testApi.getUrlBase()).isEqualTo(DEFAULT_URL_BASE);
        assertThat(testApi.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    void createApiWithExistingId() throws Exception {
        // Create the Api with an existing ID
        api.setId(1L);

        int databaseSizeBeforeCreate = apiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiRepository.findAll().size();
        // set the field null
        api.setHash(null);

        // Create the Api, which fails.

        restApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isBadRequest());

        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApis() throws Exception {
        // Initialize the database
        apiRepository.saveAndFlush(api);

        // Get all the apiList
        restApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(api.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].urlBase").value(hasItem(DEFAULT_URL_BASE)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));
    }

    @Test
    @Transactional
    void getApi() throws Exception {
        // Initialize the database
        apiRepository.saveAndFlush(api);

        // Get the api
        restApiMockMvc
            .perform(get(ENTITY_API_URL_ID, api.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(api.getId().intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.urlBase").value(DEFAULT_URL_BASE))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingApi() throws Exception {
        // Get the api
        restApiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApi() throws Exception {
        // Initialize the database
        apiRepository.saveAndFlush(api);

        int databaseSizeBeforeUpdate = apiRepository.findAll().size();

        // Update the api
        Api updatedApi = apiRepository.findById(api.getId()).get();
        // Disconnect from session so that the updates on updatedApi are not directly saved in db
        em.detach(updatedApi);
        updatedApi.hash(UPDATED_HASH).urlBase(UPDATED_URL_BASE).token(UPDATED_TOKEN);

        restApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApi))
            )
            .andExpect(status().isOk());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testApi.getUrlBase()).isEqualTo(UPDATED_URL_BASE);
        assertThat(testApi.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void putNonExistingApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, api.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiWithPatch() throws Exception {
        // Initialize the database
        apiRepository.saveAndFlush(api);

        int databaseSizeBeforeUpdate = apiRepository.findAll().size();

        // Update the api using partial update
        Api partialUpdatedApi = new Api();
        partialUpdatedApi.setId(api.getId());

        partialUpdatedApi.urlBase(UPDATED_URL_BASE);

        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApi))
            )
            .andExpect(status().isOk());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testApi.getUrlBase()).isEqualTo(UPDATED_URL_BASE);
        assertThat(testApi.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateApiWithPatch() throws Exception {
        // Initialize the database
        apiRepository.saveAndFlush(api);

        int databaseSizeBeforeUpdate = apiRepository.findAll().size();

        // Update the api using partial update
        Api partialUpdatedApi = new Api();
        partialUpdatedApi.setId(api.getId());

        partialUpdatedApi.hash(UPDATED_HASH).urlBase(UPDATED_URL_BASE).token(UPDATED_TOKEN);

        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApi))
            )
            .andExpect(status().isOk());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
        Api testApi = apiList.get(apiList.size() - 1);
        assertThat(testApi.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testApi.getUrlBase()).isEqualTo(UPDATED_URL_BASE);
        assertThat(testApi.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, api.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(api))
            )
            .andExpect(status().isBadRequest());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApi() throws Exception {
        int databaseSizeBeforeUpdate = apiRepository.findAll().size();
        api.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(api)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Api in the database
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApi() throws Exception {
        // Initialize the database
        apiRepository.saveAndFlush(api);

        int databaseSizeBeforeDelete = apiRepository.findAll().size();

        // Delete the api
        restApiMockMvc.perform(delete(ENTITY_API_URL_ID, api.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Api> apiList = apiRepository.findAll();
        assertThat(apiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
