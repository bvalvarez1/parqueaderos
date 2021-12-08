package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ec.loja.domain.UserAuthority} entity.
 */
@ApiModel(description = "The UserAuthority entity.\n@author macf")
public class UserAuthorityDTO implements Serializable {

    private Long id;

    /**
     * authority
     */
    @ApiModelProperty(value = "authority")
    private String authority;

    /**
     * activo
     */
    @ApiModelProperty(value = "activo")
    private Boolean active;

    private UserDTO user;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthorityDTO)) {
            return false;
        }

        UserAuthorityDTO userAuthorityDTO = (UserAuthorityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAuthorityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAuthorityDTO{" +
            "id=" + getId() +
            ", authority='" + getAuthority() + "'" +
            ", active='" + getActive() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
