package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Tariff} entity.
 */
@ApiModel(description = "The Tariff entity.\n@author Rene")
public class TariffDTO implements Serializable {

    private Long id;

    /**
     * fecha inicial de tarifa
     */
    @NotNull
    @ApiModelProperty(value = "fecha inicial de tarifa", required = true)
    private LocalDate initialDate;

    /**
     * fecha final de tarifa
     */
    @ApiModelProperty(value = "fecha final de tarifa")
    private LocalDate finalDate;

    private InstitutionDTO institution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
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
        if (!(o instanceof TariffDTO)) {
            return false;
        }

        TariffDTO tariffDTO = (TariffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tariffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TariffDTO{" +
            "id=" + getId() +
            ", initialDate='" + getInitialDate() + "'" +
            ", finalDate='" + getFinalDate() + "'" +
            ", institution=" + getInstitution() +
            "}";
    }
}
