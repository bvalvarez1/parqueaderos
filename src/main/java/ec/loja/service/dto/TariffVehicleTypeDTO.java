package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.TariffVehicleType} entity.
 */
@ApiModel(description = "The TariffVehicleType entity.\n@author macf")
public class TariffVehicleTypeDTO implements Serializable {

    private Long id;

    /**
     * valor minimo
     */
    @NotNull
    @ApiModelProperty(value = "valor minimo", required = true)
    private Integer minValue;

    /**
     * valor maximo
     */
    @NotNull
    @ApiModelProperty(value = "valor maximo", required = true)
    private Integer maxValue;

    /**
     * valor tarifa
     */
    @NotNull
    @ApiModelProperty(value = "valor tarifa", required = true)
    private BigDecimal value;

    private ItemCatalogueDTO vehicleType;

    private TariffDTO tariff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ItemCatalogueDTO getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(ItemCatalogueDTO vehicleType) {
        this.vehicleType = vehicleType;
    }

    public TariffDTO getTariff() {
        return tariff;
    }

    public void setTariff(TariffDTO tariff) {
        this.tariff = tariff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TariffVehicleTypeDTO)) {
            return false;
        }

        TariffVehicleTypeDTO tariffVehicleTypeDTO = (TariffVehicleTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tariffVehicleTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TariffVehicleTypeDTO{" +
            "id=" + getId() +
            ", minValue=" + getMinValue() +
            ", maxValue=" + getMaxValue() +
            ", value=" + getValue() +
            ", vehicleType=" + getVehicleType() +
            ", tariff=" + getTariff() +
            "}";
    }
}
