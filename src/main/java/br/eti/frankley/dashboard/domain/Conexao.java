package br.eti.frankley.dashboard.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conexao.
 */
@Entity
@Table(name = "conexao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conexao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "url")
    private String url;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "senha")
    private String senha;

    @Column(name = "banco")
    private String banco;

    @Column(name = "schema")
    private String schema;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conexao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public Conexao hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUrl() {
        return this.url;
    }

    public Conexao url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public Conexao usuario(String usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return this.senha;
    }

    public Conexao senha(String senha) {
        this.setSenha(senha);
        return this;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getBanco() {
        return this.banco;
    }

    public Conexao banco(String banco) {
        this.setBanco(banco);
        return this;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getSchema() {
        return this.schema;
    }

    public Conexao schema(String schema) {
        this.setSchema(schema);
        return this;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conexao)) {
            return false;
        }
        return id != null && id.equals(((Conexao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conexao{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", url='" + getUrl() + "'" +
            ", usuario='" + getUsuario() + "'" +
            ", senha='" + getSenha() + "'" +
            ", banco='" + getBanco() + "'" +
            ", schema='" + getSchema() + "'" +
            "}";
    }
}
