package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Entity AuthorityFunctionality.\n@author macf
 */
@Entity
@Table(name = "authority_functionality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthorityFunctionality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * ROL de tabla authority
     */
    @NotNull
    @Column(name = "authority", nullable = false)
    private String authority;

    /**
     * prioridad
     */
    @NotNull
    @Column(name = "priority", nullable = false)
    private Integer priority;

    /**
     * activo
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * Funcionalidad asignada a rol
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "childrens", "parent" }, allowSetters = true)
    private Functionality functionality;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AuthorityFunctionality id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return this.authority;
    }

    public AuthorityFunctionality authority(String authority) {
        this.setAuthority(authority);
        return this;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public AuthorityFunctionality priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AuthorityFunctionality active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Functionality getFunctionality() {
        return this.functionality;
    }

    public void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    public AuthorityFunctionality functionality(Functionality functionality) {
        this.setFunctionality(functionality);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityFunctionality)) {
            return false;
        }
        return id != null && id.equals(((AuthorityFunctionality) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityFunctionality{" +
            "id=" + getId() +
            ", authority='" + getAuthority() + "'" +
            ", priority=" + getPriority() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
