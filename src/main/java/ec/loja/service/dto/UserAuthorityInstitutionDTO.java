package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ec.loja.domain.UserAuthorityInstitution} entity.
 */
@ApiModel(description = "The UserAuthorityInstitution entity.\n@author macf")
public class UserAuthorityInstitutionDTO implements Serializable {

    private Long id;

    /**
     * activo
     */
    @ApiModelProperty(value = "activo")
    private Boolean active;

    private InstitutionDTO institution;

    private UserAuthorityDTO userAuthority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public InstitutionDTO getInstitution() {
        return institution;
    }

    public void setInstitution(InstitutionDTO institution) {
        this.institution = institution;
    }

    public UserAuthorityDTO getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(UserAuthorityDTO userAuthority) {
        this.userAuthority = userAuthority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthorityInstitutionDTO)) {
            return false;
        }

        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = (UserAuthorityInstitutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAuthorityInstitutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAuthorityInstitutionDTO{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", institution=" + getInstitution() +
            ", userAuthority=" + getUserAuthority() +
            "}";
    }
}
