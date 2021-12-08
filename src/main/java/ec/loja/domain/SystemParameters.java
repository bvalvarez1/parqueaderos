package ec.loja.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Entity SystemParameters.\n@author macf
 */
@Entity
@Table(name = "system_parameters")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombre
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * codigo
     */
    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * clase de java
     */
    @NotNull
    @Column(name = "clase", nullable = false)
    private String clase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemParameters id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SystemParameters name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public SystemParameters code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClase() {
        return this.clase;
    }

    public SystemParameters clase(String clase) {
        this.setClase(clase);
        return this;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemParameters)) {
            return false;
        }
        return id != null && id.equals(((SystemParameters) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemParameters{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", clase='" + getClase() + "'" +
            "}";
    }
}
