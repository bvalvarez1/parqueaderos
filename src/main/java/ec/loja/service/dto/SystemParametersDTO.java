package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.SystemParameters} entity.
 */
@ApiModel(description = "The Entity SystemParameters.\n@author macf")
public class SystemParametersDTO implements Serializable {

    private Long id;

    /**
     * nombre
     */
    @NotNull
    @ApiModelProperty(value = "nombre", required = true)
    private String name;

    /**
     * codigo
     */
    @NotNull
    @ApiModelProperty(value = "codigo", required = true)
    private String code;

    /**
     * clase de java
     */
    @NotNull
    @ApiModelProperty(value = "clase de java", required = true)
    private String clase;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemParametersDTO)) {
            return false;
        }

        SystemParametersDTO systemParametersDTO = (SystemParametersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemParametersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemParametersDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", clase='" + getClase() + "'" +
            "}";
    }
}
