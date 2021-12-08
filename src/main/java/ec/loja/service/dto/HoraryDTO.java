package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Horary} entity.
 */
@ApiModel(description = "The Horary entity.\n@author macf")
public class HoraryDTO implements Serializable {

    private Long id;

    /**
     * name
     */
    @NotNull
    @ApiModelProperty(value = "name", required = true)
    private String name;

    /**
     * startTime
     */
    @NotNull
    @ApiModelProperty(value = "startTime", required = true)
    private Instant startTime;

    /**
     * finalHour
     */
    @NotNull
    @ApiModelProperty(value = "finalHour", required = true)
    private Instant finalHour;

    private ItemCatalogueDTO status;

    private ContractDTO contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getFinalHour() {
        return finalHour;
    }

    public void setFinalHour(Instant finalHour) {
        this.finalHour = finalHour;
    }

    public ItemCatalogueDTO getStatus() {
        return status;
    }

    public void setStatus(ItemCatalogueDTO status) {
        this.status = status;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoraryDTO)) {
            return false;
        }

        HoraryDTO horaryDTO = (HoraryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, horaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoraryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", finalHour='" + getFinalHour() + "'" +
            ", status=" + getStatus() +
            ", contract=" + getContract() +
            "}";
    }
}
