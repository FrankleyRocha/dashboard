package br.eti.frankley.dashboard.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Api.
 */
@Entity
@Table(name = "api")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Api implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "url_base")
    private String urlBase;

    @Column(name = "token")
    private String token;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Api id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public Api hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUrlBase() {
        return this.urlBase;
    }

    public Api urlBase(String urlBase) {
        this.setUrlBase(urlBase);
        return this;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getToken() {
        return this.token;
    }

    public Api token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Api)) {
            return false;
        }
        return id != null && id.equals(((Api) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Api{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", urlBase='" + getUrlBase() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }
}
