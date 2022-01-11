package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Entity RecordTicket.\n@author Renmacfe
 */
@Entity
@Table(name = "record_ticket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecordTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * fecha inicio de ingreso
     */
    @NotNull
    @Column(name = "init_date", nullable = false)
    private Instant initDate;

    /**
     * fecha fin de salida
     */
    @Column(name = "end_date")
    private Instant endDate;

    /**
     * placa
     */
    @Column(name = "plate")
    private String plate;

    /**
     * tiempo de parqueo
     */
    @Column(name = "parking_time")
    private Instant parkingTime;

    /**
     * subtotal a pagar
     */
    @Column(name = "taxable_total", precision = 21, scale = 2)
    private BigDecimal taxableTotal;

    /**
     * iva a pagar
     */
    @Column(name = "taxes", precision = 21, scale = 2)
    private BigDecimal taxes;

    /**
     * iva a pagar
     */
    @Column(name = "total", precision = 21, scale = 2)
    private BigDecimal total;

    /**
     * observacion de anulacion
     */
    @Column(name = "observation")
    private String observation;

    /**
     * secuancia del tickets
     */
    @Column(name = "sequential")
    private String sequential;

    @JsonIgnoreProperties(value = { "status", "institution" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Place placeid;

    @OneToOne
    @JoinColumn(unique = true)
    private User emitter;

    @OneToOne
    @JoinColumn(unique = true)
    private User collector;

    @OneToOne
    @JoinColumn
    private User reserver;

    /**
     * RecordTicket status
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue status;

    /**
     * RecordTicket tariff applied
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vehicleType", "tariff" }, allowSetters = true)
    private TariffVehicleType tariffVehicle;

    /**
     * RecordTicket institution
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "places", "canton" }, allowSetters = true)
    private Institution institution;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecordTicket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInitDate() {
        return this.initDate;
    }

    public RecordTicket initDate(Instant initDate) {
        this.setInitDate(initDate);
        return this;
    }

    public void setInitDate(Instant initDate) {
        this.initDate = initDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public RecordTicket endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getPlate() {
        return this.plate;
    }

    public RecordTicket plate(String plate) {
        this.setPlate(plate);
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Instant getParkingTime() {
        return this.parkingTime;
    }

    public RecordTicket parkingTime(Instant parkingTime) {
        this.setParkingTime(parkingTime);
        return this;
    }

    public void setParkingTime(Instant parkingTime) {
        this.parkingTime = parkingTime;
    }

    public BigDecimal getTaxableTotal() {
        return this.taxableTotal;
    }

    public RecordTicket taxableTotal(BigDecimal taxableTotal) {
        this.setTaxableTotal(taxableTotal);
        return this;
    }

    public void setTaxableTotal(BigDecimal taxableTotal) {
        this.taxableTotal = taxableTotal;
    }

    public BigDecimal getTaxes() {
        return this.taxes;
    }

    public RecordTicket taxes(BigDecimal taxes) {
        this.setTaxes(taxes);
        return this;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public RecordTicket total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservation() {
        return this.observation;
    }

    public RecordTicket observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getSequential() {
        return this.sequential;
    }

    public RecordTicket sequential(String sequential) {
        this.setSequential(sequential);
        return this;
    }

    public void setSequential(String sequential) {
        this.sequential = sequential;
    }

    public Place getPlaceid() {
        return this.placeid;
    }

    public void setPlaceid(Place place) {
        this.placeid = place;
    }

    public RecordTicket placeid(Place place) {
        this.setPlaceid(place);
        return this;
    }

    public User getEmitter() {
        return this.emitter;
    }

    public void setEmitter(User user) {
        this.emitter = user;
    }

    public RecordTicket emitter(User user) {
        this.setEmitter(user);
        return this;
    }

    public User getCollector() {
        return this.collector;
    }

    public void setCollector(User user) {
        this.collector = user;
    }

    public RecordTicket collector(User user) {
        this.setCollector(user);
        return this;
    }

    public ItemCatalogue getStatus() {
        return this.status;
    }

    public void setStatus(ItemCatalogue itemCatalogue) {
        this.status = itemCatalogue;
    }

    public User getReserver() {
        return reserver;
    }

    public void setReserver(User reserver) {
        this.reserver = reserver;
    }

    public RecordTicket reserver(User user) {
        this.setReserver(reserver);
        return this;
    }

    public RecordTicket status(ItemCatalogue itemCatalogue) {
        this.setStatus(itemCatalogue);
        return this;
    }

    public TariffVehicleType getTariffVehicle() {
        return this.tariffVehicle;
    }

    public void setTariffVehicle(TariffVehicleType tariffVehicleType) {
        this.tariffVehicle = tariffVehicleType;
    }

    public RecordTicket tariffVehicle(TariffVehicleType tariffVehicleType) {
        this.setTariffVehicle(tariffVehicleType);
        return this;
    }

    public Institution getInstitution() {
        return this.institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public RecordTicket institution(Institution institution) {
        this.setInstitution(institution);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordTicket)) {
            return false;
        }
        return id != null && id.equals(((RecordTicket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordTicket{" +
            "id=" + getId() +
            ", initDate='" + getInitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", plate='" + getPlate() + "'" +
            ", parkingTime='" + getParkingTime() + "'" +
            ", taxableTotal=" + getTaxableTotal() +
            ", taxes=" + getTaxes() +
            ", total=" + getTotal() +
            ", observation='" + getObservation() + "'" +
            ", sequential='" + getSequential() + "'" +
            "}";
    }
}
