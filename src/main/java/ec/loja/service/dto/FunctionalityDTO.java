package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Functionality} entity.
 */
@ApiModel(description = "The Functionality entity.\n@author macf")
public class FunctionalityDTO implements Serializable {

    private Long id;

    /**
     * nombre de la funcionalidad
     */
    @NotNull
    @ApiModelProperty(value = "nombre de la funcionalidad", required = true)
    private String name;

    /**
     * descripcion
     */
    @ApiModelProperty(value = "descripcion")
    private String description;

    /**
     * icono para menu
     */
    @ApiModelProperty(value = "icono para menu")
    private String icon;

    /**
     * url de la pagina
     */
    @Size(max = 80)
    @ApiModelProperty(value = "url de la pagina")
    private String url;

    /**
     * activo
     */
    @NotNull
    @ApiModelProperty(value = "activo", required = true)
    private Boolean active;

    private FunctionalityDTO parent;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public FunctionalityDTO getParent() {
        return parent;
    }

    public void setParent(FunctionalityDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionalityDTO)) {
            return false;
        }

        FunctionalityDTO functionalityDTO = (FunctionalityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, functionalityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FunctionalityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", icon='" + getIcon() + "'" +
            ", url='" + getUrl() + "'" +
            ", active='" + getActive() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
