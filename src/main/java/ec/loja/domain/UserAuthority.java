package ec.loja.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The UserAuthority entity.\n@author macf
 */
@Entity
@Table(name = "user_authority")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * authority
     */
    @Column(name = "authority")
    private String authority;

    /**
     * activo
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * usuario a rol
     */
    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAuthority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return this.authority;
    }

    public UserAuthority authority(String authority) {
        this.setAuthority(authority);
        return this;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Boolean getActive() {
        return this.active;
    }

    public UserAuthority active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAuthority user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthority)) {
            return false;
        }
        return id != null && id.equals(((UserAuthority) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAuthority{" +
            "id=" + getId() +
            ", authority='" + getAuthority() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
