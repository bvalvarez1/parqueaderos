package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The TariffVehicleType entity.\n@author macf
 */
@Entity
@Table(name = "tariff_vehicle_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TariffVehicleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * valor minimo
     */
    @NotNull
    @Column(name = "min_value", nullable = false)
    private Integer minValue;

    /**
     * valor maximo
     */
    @NotNull
    @Column(name = "max_value", nullable = false)
    private Integer maxValue;

    /**
     * valor tarifa
     */
    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    /**
     * vehicle type
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue vehicleType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tarifs", "institution" }, allowSetters = true)
    private Tariff tariff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TariffVehicleType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinValue() {
        return this.minValue;
    }

    public TariffVehicleType minValue(Integer minValue) {
        this.setMinValue(minValue);
        return this;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return this.maxValue;
    }

    public TariffVehicleType maxValue(Integer maxValue) {
        this.setMaxValue(maxValue);
        return this;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public TariffVehicleType value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ItemCatalogue getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(ItemCatalogue itemCatalogue) {
        this.vehicleType = itemCatalogue;
    }

    public TariffVehicleType vehicleType(ItemCatalogue itemCatalogue) {
        this.setVehicleType(itemCatalogue);
        return this;
    }

    public Tariff getTariff() {
        return this.tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public TariffVehicleType tariff(Tariff tariff) {
        this.setTariff(tariff);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TariffVehicleType)) {
            return false;
        }
        return id != null && id.equals(((TariffVehicleType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TariffVehicleType{" +
            "id=" + getId() +
            ", minValue=" + getMinValue() +
            ", maxValue=" + getMaxValue() +
            ", value=" + getValue() +
            "}";
    }
}
