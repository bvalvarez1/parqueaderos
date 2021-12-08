package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Place} entity.
 */
@ApiModel(description = "The Place entity.\n@author A true hipster")
public class PlaceDTO implements Serializable {

    private Long id;

    /**
     * number
     */
    @NotNull
    @ApiModelProperty(value = "number", required = true)
    private String number;

    private ItemCatalogueDTO status;

    private InstitutionDTO institution;

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

    public ItemCatalogueDTO getStatus() {
        return status;
    }

    public void setStatus(ItemCatalogueDTO status) {
        this.status = status;
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
        if (!(o instanceof PlaceDTO)) {
            return false;
        }

        PlaceDTO placeDTO = (PlaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, placeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", status=" + getStatus() +
            ", institution=" + getInstitution() +
            "}";
    }
}
