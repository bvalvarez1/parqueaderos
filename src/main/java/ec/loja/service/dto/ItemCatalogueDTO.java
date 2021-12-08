package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.ItemCatalogue} entity.
 */
@ApiModel(description = "The Entity entity.\n@author A true hipster")
public class ItemCatalogueDTO implements Serializable {

    private Long id;

    /**
     * nombre de itemcatalogo
     */
    @NotNull
    @ApiModelProperty(value = "nombre de itemcatalogo", required = true)
    private String name;

    /**
     * codigo de itemcatalogo
     */
    @NotNull
    @ApiModelProperty(value = "codigo de itemcatalogo", required = true)
    private String code;

    /**
     * descripcion de itemcatalogo
     */
    @ApiModelProperty(value = "descripcion de itemcatalogo")
    private String description;

    /**
     * codigo de catalogo
     */
    @NotNull
    @ApiModelProperty(value = "codigo de catalogo", required = true)
    private String catalogCode;

    /**
     * activo
     */
    @ApiModelProperty(value = "activo")
    private Boolean active;

    private CatalogueDTO catalogue;

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

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CatalogueDTO getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogueDTO catalogue) {
        this.catalogue = catalogue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemCatalogueDTO)) {
            return false;
        }

        ItemCatalogueDTO itemCatalogueDTO = (ItemCatalogueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemCatalogueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCatalogueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", catalogCode='" + getCatalogCode() + "'" +
            ", active='" + getActive() + "'" +
            ", catalogue=" + getCatalogue() +
            "}";
    }
}
