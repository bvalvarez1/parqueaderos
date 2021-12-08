package ec.loja.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Contract} entity.
 */
public class ContractDTO implements Serializable {

    private Long id;

    /**
     * number
     */
    @NotNull
    @ApiModelProperty(value = "number", required = true)
    private String number;

    /**
     * inicio del contrato
     */
    @NotNull
    @ApiModelProperty(value = "inicio del contrato", required = true)
    private LocalDate initDate;

    /**
     * fin del contrato
     */
    @NotNull
    @ApiModelProperty(value = "fin del contrato", required = true)
    private LocalDate endDate;

    /**
     * valor del contrato
     */
    @NotNull
    @ApiModelProperty(value = "valor del contrato", required = true)
    private BigDecimal value;

    /**
     * hours
     */
    @ApiModelProperty(value = "hours")
    private BigDecimal hours;

    private ItemCatalogueDTO status;

    private PersonDTO contractor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public ItemCatalogueDTO getStatus() {
        return status;
    }

    public void setStatus(ItemCatalogueDTO status) {
        this.status = status;
    }

    public PersonDTO getContractor() {
        return contractor;
    }

    public void setContractor(PersonDTO contractor) {
        this.contractor = contractor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractDTO)) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", initDate='" + getInitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", value=" + getValue() +
            ", hours=" + getHours() +
            ", status=" + getStatus() +
            ", contractor=" + getContractor() +
            "}";
    }
}
