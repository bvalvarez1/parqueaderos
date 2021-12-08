package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Record} entity.
 */
@ApiModel(description = "The Entity record.\n@author Renmacfe")
public class RecordDTO implements Serializable {

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

    private ItemCatalogueDTO status;

    private TariffVehicleTypeDTO tariffVehicle;

    private InstitutionDTO institution;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordDTO)) {
            return false;
        }

        RecordDTO recordDTO = (RecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordDTO{" +
            "id=" + getId() +
            ", initDate='" + getInitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", plate='" + getPlate() + "'" +
            ", parkingTime='" + getParkingTime() + "'" +
            ", taxableTotal=" + getTaxableTotal() +
            ", taxes=" + getTaxes() +
            ", total=" + getTotal() +
            ", status=" + getStatus() +
            ", tariffVehicle=" + getTariffVehicle() +
            ", institution=" + getInstitution() +
            "}";
    }
}
