package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * number
     */
    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    /**
     * inicio del contrato
     */
    @NotNull
    @Column(name = "init_date", nullable = false)
    private LocalDate initDate;

    /**
     * fin del contrato
     */
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * valor del contrato
     */
    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    /**
     * hours
     */
    @Column(name = "hours", precision = 21, scale = 2)
    private BigDecimal hours;

    /**
     * horarios de contract
     */
    @OneToMany(mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "status", "contract" }, allowSetters = true)
    private Set<Horary> horaries = new HashSet<>();

    /**
     * estado del contrato
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue status;

    /**
     * persona del contrato
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "identificationType" }, allowSetters = true)
    private Person contractor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public Contract number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getInitDate() {
        return this.initDate;
    }

    public Contract initDate(LocalDate initDate) {
        this.setInitDate(initDate);
        return this;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Contract endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Contract value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getHours() {
        return this.hours;
    }

    public Contract hours(BigDecimal hours) {
        this.setHours(hours);
        return this;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public Set<Horary> getHoraries() {
        return this.horaries;
    }

    public void setHoraries(Set<Horary> horaries) {
        if (this.horaries != null) {
            this.horaries.forEach(i -> i.setContract(null));
        }
        if (horaries != null) {
            horaries.forEach(i -> i.setContract(this));
        }
        this.horaries = horaries;
    }

    public Contract horaries(Set<Horary> horaries) {
        this.setHoraries(horaries);
        return this;
    }

    public Contract addHoraries(Horary horary) {
        this.horaries.add(horary);
        horary.setContract(this);
        return this;
    }

    public Contract removeHoraries(Horary horary) {
        this.horaries.remove(horary);
        horary.setContract(null);
        return this;
    }

    public ItemCatalogue getStatus() {
        return this.status;
    }

    public void setStatus(ItemCatalogue itemCatalogue) {
        this.status = itemCatalogue;
    }

    public Contract status(ItemCatalogue itemCatalogue) {
        this.setStatus(itemCatalogue);
        return this;
    }

    public Person getContractor() {
        return this.contractor;
    }

    public void setContractor(Person person) {
        this.contractor = person;
    }

    public Contract contractor(Person person) {
        this.setContractor(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return id != null && id.equals(((Contract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", initDate='" + getInitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", value=" + getValue() +
            ", hours=" + getHours() +
            "}";
    }
}
