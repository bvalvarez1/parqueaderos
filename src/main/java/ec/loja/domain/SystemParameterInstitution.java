package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Entity SystemParameterInstitution.\n@author macf
 */
@Entity
@Table(name = "system_parameter_institution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemParameterInstitution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * valor del campo por empresa
     */
    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    /**
     * determinar si esta activo o no el parametro por empresa
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * parameter of institution
     */
    @ManyToOne(optional = false)
    @NotNull
    private SystemParameters parameter;

    /**
     * institution of parameter
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "places", "canton" }, allowSetters = true)
    private Institution institution;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemParameterInstitution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public SystemParameterInstitution value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SystemParameterInstitution active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SystemParameters getParameter() {
        return this.parameter;
    }

    public void setParameter(SystemParameters systemParameters) {
        this.parameter = systemParameters;
    }

    public SystemParameterInstitution parameter(SystemParameters systemParameters) {
        this.setParameter(systemParameters);
        return this;
    }

    public Institution getInstitution() {
        return this.institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public SystemParameterInstitution institution(Institution institution) {
        this.setInstitution(institution);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemParameterInstitution)) {
            return false;
        }
        return id != null && id.equals(((SystemParameterInstitution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemParameterInstitution{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
