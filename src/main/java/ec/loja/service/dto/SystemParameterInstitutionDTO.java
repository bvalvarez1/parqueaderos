package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.SystemParameterInstitution} entity.
 */
@ApiModel(description = "The Entity SystemParameterInstitution.\n@author macf")
public class SystemParameterInstitutionDTO implements Serializable {

    private Long id;

    /**
     * valor del campo por empresa
     */
    @NotNull
    @ApiModelProperty(value = "valor del campo por empresa", required = true)
    private String value;

    /**
     * determinar si esta activo o no el parametro por empresa
     */
    @ApiModelProperty(value = "determinar si esta activo o no el parametro por empresa")
    private Boolean active;

    private SystemParametersDTO parameter;

    private InstitutionDTO institution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SystemParametersDTO getParameter() {
        return parameter;
    }

    public void setParameter(SystemParametersDTO parameter) {
        this.parameter = parameter;
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
        if (!(o instanceof SystemParameterInstitutionDTO)) {
            return false;
        }

        SystemParameterInstitutionDTO systemParameterInstitutionDTO = (SystemParameterInstitutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemParameterInstitutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemParameterInstitutionDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", active='" + getActive() + "'" +
            ", parameter=" + getParameter() +
            ", institution=" + getInstitution() +
            "}";
    }
}
