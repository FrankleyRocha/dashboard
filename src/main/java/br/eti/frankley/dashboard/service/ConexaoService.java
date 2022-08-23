package br.eti.frankley.dashboard.service;

import br.eti.frankley.dashboard.domain.Conexao;
import br.eti.frankley.dashboard.repository.ConexaoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conexao}.
 */
@Service
@Transactional
public class ConexaoService {

    private final Logger log = LoggerFactory.getLogger(ConexaoService.class);

    private final ConexaoRepository conexaoRepository;

    public ConexaoService(ConexaoRepository conexaoRepository) {
        this.conexaoRepository = conexaoRepository;
    }

    /**
     * Save a conexao.
     *
     * @param conexao the entity to save.
     * @return the persisted entity.
     */
    public Conexao save(Conexao conexao) {
        log.debug("Request to save Conexao : {}", conexao);
        return conexaoRepository.save(conexao);
    }

    /**
     * Update a conexao.
     *
     * @param conexao the entity to save.
     * @return the persisted entity.
     */
    public Conexao update(Conexao conexao) {
        log.debug("Request to save Conexao : {}", conexao);
        return conexaoRepository.save(conexao);
    }

    /**
     * Partially update a conexao.
     *
     * @param conexao the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Conexao> partialUpdate(Conexao conexao) {
        log.debug("Request to partially update Conexao : {}", conexao);

        return conexaoRepository
            .findById(conexao.getId())
            .map(existingConexao -> {
                if (conexao.getHash() != null) {
                    existingConexao.setHash(conexao.getHash());
                }
                if (conexao.getUrl() != null) {
                    existingConexao.setUrl(conexao.getUrl());
                }
                if (conexao.getUsuario() != null) {
                    existingConexao.setUsuario(conexao.getUsuario());
                }
                if (conexao.getSenha() != null) {
                    existingConexao.setSenha(conexao.getSenha());
                }
                if (conexao.getBanco() != null) {
                    existingConexao.setBanco(conexao.getBanco());
                }
                if (conexao.getSchema() != null) {
                    existingConexao.setSchema(conexao.getSchema());
                }

                return existingConexao;
            })
            .map(conexaoRepository::save);
    }

    /**
     * Get all the conexaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Conexao> findAll(Pageable pageable) {
        log.debug("Request to get all Conexaos");
        return conexaoRepository.findAll(pageable);
    }

    /**
     * Get one conexao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Conexao> findOne(Long id) {
        log.debug("Request to get Conexao : {}", id);
        return conexaoRepository.findById(id);
    }

    /**
     * Delete the conexao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conexao : {}", id);
        conexaoRepository.deleteById(id);
    }
}
