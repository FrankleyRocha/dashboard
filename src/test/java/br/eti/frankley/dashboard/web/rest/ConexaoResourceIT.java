package br.eti.frankley.dashboard.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.eti.frankley.dashboard.IntegrationTest;
import br.eti.frankley.dashboard.domain.Conexao;
import br.eti.frankley.dashboard.repository.ConexaoRepository;
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
 * Integration tests for the {@link ConexaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConexaoResourceIT {

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_SENHA = "BBBBBBBBBB";

    private static final String DEFAULT_BANCO = "AAAAAAAAAA";
    private static final String UPDATED_BANCO = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEMA = "AAAAAAAAAA";
    private static final String UPDATED_SCHEMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conexaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConexaoRepository conexaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConexaoMockMvc;

    private Conexao conexao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conexao createEntity(EntityManager em) {
        Conexao conexao = new Conexao()
            .hash(DEFAULT_HASH)
            .url(DEFAULT_URL)
            .usuario(DEFAULT_USUARIO)
            .senha(DEFAULT_SENHA)
            .banco(DEFAULT_BANCO)
            .schema(DEFAULT_SCHEMA);
        return conexao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conexao createUpdatedEntity(EntityManager em) {
        Conexao conexao = new Conexao()
            .hash(UPDATED_HASH)
            .url(UPDATED_URL)
            .usuario(UPDATED_USUARIO)
            .senha(UPDATED_SENHA)
            .banco(UPDATED_BANCO)
            .schema(UPDATED_SCHEMA);
        return conexao;
    }

    @BeforeEach
    public void initTest() {
        conexao = createEntity(em);
    }

    @Test
    @Transactional
    void createConexao() throws Exception {
        int databaseSizeBeforeCreate = conexaoRepository.findAll().size();
        // Create the Conexao
        restConexaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conexao)))
            .andExpect(status().isCreated());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeCreate + 1);
        Conexao testConexao = conexaoList.get(conexaoList.size() - 1);
        assertThat(testConexao.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testConexao.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testConexao.getUsuario()).isEqualTo(DEFAULT_USUARIO);
        assertThat(testConexao.getSenha()).isEqualTo(DEFAULT_SENHA);
        assertThat(testConexao.getBanco()).isEqualTo(DEFAULT_BANCO);
        assertThat(testConexao.getSchema()).isEqualTo(DEFAULT_SCHEMA);
    }

    @Test
    @Transactional
    void createConexaoWithExistingId() throws Exception {
        // Create the Conexao with an existing ID
        conexao.setId(1L);

        int databaseSizeBeforeCreate = conexaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConexaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conexao)))
            .andExpect(status().isBadRequest());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = conexaoRepository.findAll().size();
        // set the field null
        conexao.setHash(null);

        // Create the Conexao, which fails.

        restConexaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conexao)))
            .andExpect(status().isBadRequest());

        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConexaos() throws Exception {
        // Initialize the database
        conexaoRepository.saveAndFlush(conexao);

        // Get all the conexaoList
        restConexaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conexao.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].usuario").value(hasItem(DEFAULT_USUARIO)))
            .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA)))
            .andExpect(jsonPath("$.[*].banco").value(hasItem(DEFAULT_BANCO)))
            .andExpect(jsonPath("$.[*].schema").value(hasItem(DEFAULT_SCHEMA)));
    }

    @Test
    @Transactional
    void getConexao() throws Exception {
        // Initialize the database
        conexaoRepository.saveAndFlush(conexao);

        // Get the conexao
        restConexaoMockMvc
            .perform(get(ENTITY_API_URL_ID, conexao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conexao.getId().intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.usuario").value(DEFAULT_USUARIO))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA))
            .andExpect(jsonPath("$.banco").value(DEFAULT_BANCO))
            .andExpect(jsonPath("$.schema").value(DEFAULT_SCHEMA));
    }

    @Test
    @Transactional
    void getNonExistingConexao() throws Exception {
        // Get the conexao
        restConexaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConexao() throws Exception {
        // Initialize the database
        conexaoRepository.saveAndFlush(conexao);

        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();

        // Update the conexao
        Conexao updatedConexao = conexaoRepository.findById(conexao.getId()).get();
        // Disconnect from session so that the updates on updatedConexao are not directly saved in db
        em.detach(updatedConexao);
        updatedConexao
            .hash(UPDATED_HASH)
            .url(UPDATED_URL)
            .usuario(UPDATED_USUARIO)
            .senha(UPDATED_SENHA)
            .banco(UPDATED_BANCO)
            .schema(UPDATED_SCHEMA);

        restConexaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConexao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConexao))
            )
            .andExpect(status().isOk());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
        Conexao testConexao = conexaoList.get(conexaoList.size() - 1);
        assertThat(testConexao.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testConexao.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testConexao.getUsuario()).isEqualTo(UPDATED_USUARIO);
        assertThat(testConexao.getSenha()).isEqualTo(UPDATED_SENHA);
        assertThat(testConexao.getBanco()).isEqualTo(UPDATED_BANCO);
        assertThat(testConexao.getSchema()).isEqualTo(UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    void putNonExistingConexao() throws Exception {
        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();
        conexao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConexaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conexao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conexao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConexao() throws Exception {
        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();
        conexao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConexaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conexao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConexao() throws Exception {
        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();
        conexao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConexaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conexao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConexaoWithPatch() throws Exception {
        // Initialize the database
        conexaoRepository.saveAndFlush(conexao);

        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();

        // Update the conexao using partial update
        Conexao partialUpdatedConexao = new Conexao();
        partialUpdatedConexao.setId(conexao.getId());

        partialUpdatedConexao.hash(UPDATED_HASH);

        restConexaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConexao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConexao))
            )
            .andExpect(status().isOk());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
        Conexao testConexao = conexaoList.get(conexaoList.size() - 1);
        assertThat(testConexao.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testConexao.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testConexao.getUsuario()).isEqualTo(DEFAULT_USUARIO);
        assertThat(testConexao.getSenha()).isEqualTo(DEFAULT_SENHA);
        assertThat(testConexao.getBanco()).isEqualTo(DEFAULT_BANCO);
        assertThat(testConexao.getSchema()).isEqualTo(DEFAULT_SCHEMA);
    }

    @Test
    @Transactional
    void fullUpdateConexaoWithPatch() throws Exception {
        // Initialize the database
        conexaoRepository.saveAndFlush(conexao);

        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();

        // Update the conexao using partial update
        Conexao partialUpdatedConexao = new Conexao();
        partialUpdatedConexao.setId(conexao.getId());

        partialUpdatedConexao
            .hash(UPDATED_HASH)
            .url(UPDATED_URL)
            .usuario(UPDATED_USUARIO)
            .senha(UPDATED_SENHA)
            .banco(UPDATED_BANCO)
            .schema(UPDATED_SCHEMA);

        restConexaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConexao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConexao))
            )
            .andExpect(status().isOk());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
        Conexao testConexao = conexaoList.get(conexaoList.size() - 1);
        assertThat(testConexao.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testConexao.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testConexao.getUsuario()).isEqualTo(UPDATED_USUARIO);
        assertThat(testConexao.getSenha()).isEqualTo(UPDATED_SENHA);
        assertThat(testConexao.getBanco()).isEqualTo(UPDATED_BANCO);
        assertThat(testConexao.getSchema()).isEqualTo(UPDATED_SCHEMA);
    }

    @Test
    @Transactional
    void patchNonExistingConexao() throws Exception {
        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();
        conexao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConexaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conexao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conexao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConexao() throws Exception {
        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();
        conexao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConexaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conexao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConexao() throws Exception {
        int databaseSizeBeforeUpdate = conexaoRepository.findAll().size();
        conexao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConexaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conexao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conexao in the database
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConexao() throws Exception {
        // Initialize the database
        conexaoRepository.saveAndFlush(conexao);

        int databaseSizeBeforeDelete = conexaoRepository.findAll().size();

        // Delete the conexao
        restConexaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, conexao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conexao> conexaoList = conexaoRepository.findAll();
        assertThat(conexaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
