package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ec.loja.domain.Person} entity.
 */
@ApiModel(description = "The Person entity.\n@author macf")
public class PersonDTO implements Serializable {

    private Long id;

    /**
     * nombres completo
     */
    @ApiModelProperty(value = "nombres completo")
    private String fullName;

    /**
     * nombre
     */
    @NotNull
    @ApiModelProperty(value = "nombre", required = true)
    private String fistName;

    /**
     * apellido
     */
    @ApiModelProperty(value = "apellido")
    private String surname;

    /**
     * email
     */
    @ApiModelProperty(value = "email")
    private String email;

    /**
     * numero identificacion
     */
    @NotNull
    @ApiModelProperty(value = "numero identificacion", required = true)
    private String identificactionNumber;

    /**
     * numero telefono convencional
     */
    @ApiModelProperty(value = "numero telefono convencional")
    private String telephoneNumber;

    private UserDTO user;

    private ItemCatalogueDTO identificationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificactionNumber() {
        return identificactionNumber;
    }

    public void setIdentificactionNumber(String identificactionNumber) {
        this.identificactionNumber = identificactionNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ItemCatalogueDTO getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(ItemCatalogueDTO identificationType) {
        this.identificationType = identificationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDTO)) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", fistName='" + getFistName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", identificactionNumber='" + getIdentificactionNumber() + "'" +
            ", telephoneNumber='" + getTelephoneNumber() + "'" +
            ", user=" + getUser() +
            ", identificationType=" + getIdentificationType() +
            "}";
    }
}
