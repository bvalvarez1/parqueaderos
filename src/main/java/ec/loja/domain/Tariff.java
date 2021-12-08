package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Tariff entity.\n@author Rene
 */
@Entity
@Table(name = "tariff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tariff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * fecha inicial de tarifa
     */
    @NotNull
    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;

    /**
     * fecha final de tarifa
     */
    @Column(name = "final_date")
    private LocalDate finalDate;

    /**
     * tarifa de acuerdo al vehiculo
     */
    @OneToMany(mappedBy = "tariff")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicleType", "tariff" }, allowSetters = true)
    private Set<TariffVehicleType> tarifs = new HashSet<>();

    /**
     * institution of tariff
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "places", "canton" }, allowSetters = true)
    private Institution institution;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tariff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInitialDate() {
        return this.initialDate;
    }

    public Tariff initialDate(LocalDate initialDate) {
        this.setInitialDate(initialDate);
        return this;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return this.finalDate;
    }

    public Tariff finalDate(LocalDate finalDate) {
        this.setFinalDate(finalDate);
        return this;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public Set<TariffVehicleType> getTarifs() {
        return this.tarifs;
    }

    public void setTarifs(Set<TariffVehicleType> tariffVehicleTypes) {
        if (this.tarifs != null) {
            this.tarifs.forEach(i -> i.setTariff(null));
        }
        if (tariffVehicleTypes != null) {
            tariffVehicleTypes.forEach(i -> i.setTariff(this));
        }
        this.tarifs = tariffVehicleTypes;
    }

    public Tariff tarifs(Set<TariffVehicleType> tariffVehicleTypes) {
        this.setTarifs(tariffVehicleTypes);
        return this;
    }

    public Tariff addTarifs(TariffVehicleType tariffVehicleType) {
        this.tarifs.add(tariffVehicleType);
        tariffVehicleType.setTariff(this);
        return this;
    }

    public Tariff removeTarifs(TariffVehicleType tariffVehicleType) {
        this.tarifs.remove(tariffVehicleType);
        tariffVehicleType.setTariff(null);
        return this;
    }

    public Institution getInstitution() {
        return this.institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Tariff institution(Institution institution) {
        this.setInstitution(institution);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tariff)) {
            return false;
        }
        return id != null && id.equals(((Tariff) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tariff{" +
            "id=" + getId() +
            ", initialDate='" + getInitialDate() + "'" +
            ", finalDate='" + getFinalDate() + "'" +
            "}";
    }
}
