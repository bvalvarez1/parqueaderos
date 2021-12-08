package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The UserAuthorityInstitution entity.\n@author macf
 */
@Entity
@Table(name = "user_authority_institution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAuthorityInstitution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * activo
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * institution a rol
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "places", "canton" }, allowSetters = true)
    private Institution institution;

    /**
     * user authority a institution
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private UserAuthority userAuthority;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAuthorityInstitution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return this.active;
    }

    public UserAuthorityInstitution active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Institution getInstitution() {
        return this.institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public UserAuthorityInstitution institution(Institution institution) {
        this.setInstitution(institution);
        return this;
    }

    public UserAuthority getUserAuthority() {
        return this.userAuthority;
    }

    public void setUserAuthority(UserAuthority userAuthority) {
        this.userAuthority = userAuthority;
    }

    public UserAuthorityInstitution userAuthority(UserAuthority userAuthority) {
        this.setUserAuthority(userAuthority);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthorityInstitution)) {
            return false;
        }
        return id != null && id.equals(((UserAuthorityInstitution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAuthorityInstitution{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
