package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Catalogue} entity.
 */
@ApiModel(description = "The Entity catalog\n@author macf")
public class CatalogueDTO implements Serializable {

    private Long id;

    /**
     * nombre de catalogo
     */
    @NotNull
    @ApiModelProperty(value = "nombre de catalogo", required = true)
    private String name;

    /**
     * codigo de catalogo-unico
     */
    @NotNull
    @ApiModelProperty(value = "codigo de catalogo-unico", required = true)
    private String code;

    /**
     * descripcion de catalogo
     */
    @ApiModelProperty(value = "descripcion de catalogo")
    private String description;

    /**
     * activo
     */
    @ApiModelProperty(value = "activo")
    private Boolean active;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogueDTO)) {
            return false;
        }

        CatalogueDTO catalogueDTO = (CatalogueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
