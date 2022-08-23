package br.eti.frankley.dashboard.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.eti.frankley.dashboard.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConexaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conexao.class);
        Conexao conexao1 = new Conexao();
        conexao1.setId(1L);
        Conexao conexao2 = new Conexao();
        conexao2.setId(conexao1.getId());
        assertThat(conexao1).isEqualTo(conexao2);
        conexao2.setId(2L);
        assertThat(conexao1).isNotEqualTo(conexao2);
        conexao1.setId(null);
        assertThat(conexao1).isNotEqualTo(conexao2);
    }
}
