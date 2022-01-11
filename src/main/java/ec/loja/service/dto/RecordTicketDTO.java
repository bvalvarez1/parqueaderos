package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.RecordTicket} entity.
 */
@ApiModel(description = "The Entity RecordTicket.\n@author Renmacfe")
public class RecordTicketDTO implements Serializable {

    private Long id;

    /**
     * fecha inicio de ingreso
     */
    @NotNull
    @ApiModelProperty(value = "fecha inicio de ingreso", required = true)
    private Instant initDate;

    /**
     * fecha fin de salida
     */
    @ApiModelProperty(value = "fecha fin de salida")
    private Instant endDate;

    /**
     * placa
     */
    @ApiModelProperty(value = "placa")
    private String plate;

    /**
     * tiempo de parqueo
     */
    @ApiModelProperty(value = "tiempo de parqueo")
    private Instant parkingTime;

    /**
     * subtotal a pagar
     */
    @ApiModelProperty(value = "subtotal a pagar")
    private BigDecimal taxableTotal;

    /**
     * iva a pagar
     */
    @ApiModelProperty(value = "iva a pagar")
    private BigDecimal taxes;

    /**
     * iva a pagar
     */
    @ApiModelProperty(value = "iva a pagar")
    private BigDecimal total;

    /**
     * observacion de anulacion
     */
    @ApiModelProperty(value = "observacion de anulacion")
    private String observation;

    /**
     * secuancia del tickets
     */
    @ApiModelProperty(value = "secuancia del tickets")
    private String sequential;

    private PlaceDTO placeid;

    private UserDTO emitter;

    private UserDTO collector;

    private UserDTO reserver;

    private ItemCatalogueDTO status;

    private TariffVehicleTypeDTO tariffVehicle;

    private InstitutionDTO institution;
    //agregados para UI
    private long days;
    private int hours;
    private int minutes;
    private long seconds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInitDate() {
        return initDate;
    }

    public void setInitDate(Instant initDate) {
        this.initDate = initDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Instant getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(Instant parkingTime) {
        this.parkingTime = parkingTime;
    }

    public BigDecimal getTaxableTotal() {
        return taxableTotal;
    }

    public void setTaxableTotal(BigDecimal taxableTotal) {
        this.taxableTotal = taxableTotal;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getSequential() {
        return sequential;
    }

    public void setSequential(String sequential) {
        this.sequential = sequential;
    }

    public PlaceDTO getPlaceid() {
        return placeid;
    }

    public void setPlaceid(PlaceDTO placeid) {
        this.placeid = placeid;
    }

    public UserDTO getEmitter() {
        return emitter;
    }

    public void setEmitter(UserDTO emitter) {
        this.emitter = emitter;
    }

    public UserDTO getCollector() {
        return collector;
    }

    public void setCollector(UserDTO collector) {
        this.collector = collector;
    }

    public ItemCatalogueDTO getStatus() {
        return status;
    }

    public void setStatus(ItemCatalogueDTO status) {
        this.status = status;
    }

    public TariffVehicleTypeDTO getTariffVehicle() {
        return tariffVehicle;
    }

    public void setTariffVehicle(TariffVehicleTypeDTO tariffVehicle) {
        this.tariffVehicle = tariffVehicle;
    }

    public InstitutionDTO getInstitution() {
        return institution;
    }

    public void setInstitution(InstitutionDTO institution) {
        this.institution = institution;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordTicketDTO)) {
            return false;
        }

        RecordTicketDTO recordTicketDTO = (RecordTicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recordTicketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public UserDTO getReserver() {
        return reserver;
    }

    public void setReserver(UserDTO reserver) {
        this.reserver = reserver;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordTicketDTO{" +
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
            ", placeid=" + getPlaceid() +
            ", emitter=" + getEmitter() +
            ", collector=" + getCollector() +
            ", reserver=" + getReserver() +
            ", status=" + getStatus() +
            ", tariffVehicle=" + getTariffVehicle() +
            ", institution=" + getInstitution() +
            "}";
    }
}
