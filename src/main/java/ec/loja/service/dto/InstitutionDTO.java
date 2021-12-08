package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Institution} entity.
 */
@ApiModel(description = "The Institution entity.\n@author macf")
public class InstitutionDTO implements Serializable {

    private Long id;

    /**
     * nombre de la empresa/institucion
     */
    @NotNull
    @ApiModelProperty(value = "nombre de la empresa/institucion", required = true)
    private String name;

    /**
     * Direccion
     */
    @ApiModelProperty(value = "Direccion")
    private String address;

    /**
     * espacios disponibles
     */
    @NotNull
    @ApiModelProperty(value = "espacios disponibles", required = true)
    private Integer placesNumber;

    /**
     * ruc de la empresa
     */
    @NotNull
    @ApiModelProperty(value = "ruc de la empresa", required = true)
    private String ruc;

    /**
     * latitud
     */
    @ApiModelProperty(value = "latitud")
    private BigDecimal latitude;

    /**
     * longitud
     */
    @ApiModelProperty(value = "longitud")
    private BigDecimal longitude;

    /**
     * for sequential
     */
    @NotNull
    @ApiModelProperty(value = "for ticket", required = true)
    private String acronym;

    /**
     * for sequential
     */
    @NotNull
    @ApiModelProperty(value = "for sequential")
    private String sequencename;

    private ItemCatalogueDTO canton;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPlacesNumber() {
        return placesNumber;
    }

    public void setPlacesNumber(Integer placesNumber) {
        this.placesNumber = placesNumber;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getSequencename() {
        return sequencename;
    }

    public void setSequencename(String sequencename) {
        this.sequencename = sequencename;
    }

    public ItemCatalogueDTO getCanton() {
        return canton;
    }

    public void setCanton(ItemCatalogueDTO canton) {
        this.canton = canton;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionDTO)) {
            return false;
        }

        InstitutionDTO institutionDTO = (InstitutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, institutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", placesNumber=" + getPlacesNumber() +
            ", ruc='" + getRuc() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", acronym='" + getAcronym() + "'" +
            ", sequencename='" + getSequencename() + "'" +
            ", canton=" + getCanton() +
            "}";
    }
}
