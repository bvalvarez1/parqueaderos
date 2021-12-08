package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Horary entity.\n@author macf
 */
@Entity
@Table(name = "horary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Horary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * name
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * startTime
     */
    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    /**
     * finalHour
     */
    @NotNull
    @Column(name = "final_hour", nullable = false)
    private Instant finalHour;

    /**
     * estado of horario
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "horaries", "status", "contractor" }, allowSetters = true)
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Horary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Horary name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Horary startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getFinalHour() {
        return this.finalHour;
    }

    public Horary finalHour(Instant finalHour) {
        this.setFinalHour(finalHour);
        return this;
    }

    public void setFinalHour(Instant finalHour) {
        this.finalHour = finalHour;
    }

    public ItemCatalogue getStatus() {
        return this.status;
    }

    public void setStatus(ItemCatalogue itemCatalogue) {
        this.status = itemCatalogue;
    }

    public Horary status(ItemCatalogue itemCatalogue) {
        this.setStatus(itemCatalogue);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Horary contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Horary)) {
            return false;
        }
        return id != null && id.equals(((Horary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Horary{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", finalHour='" + getFinalHour() + "'" +
            "}";
    }
}
