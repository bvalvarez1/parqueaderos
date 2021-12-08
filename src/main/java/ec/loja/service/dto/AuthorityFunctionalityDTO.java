package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.AuthorityFunctionality} entity.
 */
@ApiModel(description = "The Entity AuthorityFunctionality.\n@author macf")
public class AuthorityFunctionalityDTO implements Serializable {

    private Long id;

    /**
     * ROL de tabla authority
     */
    @NotNull
    @ApiModelProperty(value = "ROL de tabla authority", required = true)
    private String authority;

    /**
     * prioridad
     */
    @NotNull
    @ApiModelProperty(value = "prioridad", required = true)
    private Integer priority;

    /**
     * activo
     */
    @ApiModelProperty(value = "activo")
    private Boolean active;

    private FunctionalityDTO functionality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public FunctionalityDTO getFunctionality() {
        return functionality;
    }

    public void setFunctionality(FunctionalityDTO functionality) {
        this.functionality = functionality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityFunctionalityDTO)) {
            return false;
        }

        AuthorityFunctionalityDTO authorityFunctionalityDTO = (AuthorityFunctionalityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorityFunctionalityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityFunctionalityDTO{" +
            "id=" + getId() +
            ", authority='" + getAuthority() + "'" +
            ", priority=" + getPriority() +
            ", active='" + getActive() + "'" +
            ", functionality=" + getFunctionality() +
            "}";
    }
}
