package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Functionality entity.\n@author macf
 */
@Entity
@Table(name = "functionality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Functionality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombre de la funcionalidad
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * descripcion
     */
    @Column(name = "description")
    private String description;

    /**
     * icono para menu
     */
    @Column(name = "icon")
    private String icon;

    /**
     * url de la pagina
     */
    @Size(max = 80)
    @Column(name = "url", length = 80)
    private String url;

    /**
     * activo
     */
    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    /**
     * childrens functionalities
     */
    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "childrens", "parent" }, allowSetters = true)
    private Set<Functionality> childrens = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "childrens", "parent" }, allowSetters = true)
    private Functionality parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Functionality id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Functionality name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Functionality description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return this.icon;
    }

    public Functionality icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return this.url;
    }

    public Functionality url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Functionality active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Functionality> getChildrens() {
        return this.childrens;
    }

    public void setChildrens(Set<Functionality> functionalities) {
        if (this.childrens != null) {
            this.childrens.forEach(i -> i.setParent(null));
        }
        if (functionalities != null) {
            functionalities.forEach(i -> i.setParent(this));
        }
        this.childrens = functionalities;
    }

    public Functionality childrens(Set<Functionality> functionalities) {
        this.setChildrens(functionalities);
        return this;
    }

    public Functionality addChildrens(Functionality functionality) {
        this.childrens.add(functionality);
        functionality.setParent(this);
        return this;
    }

    public Functionality removeChildrens(Functionality functionality) {
        this.childrens.remove(functionality);
        functionality.setParent(null);
        return this;
    }

    public Functionality getParent() {
        return this.parent;
    }

    public void setParent(Functionality functionality) {
        this.parent = functionality;
    }

    public Functionality parent(Functionality functionality) {
        this.setParent(functionality);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Functionality)) {
            return false;
        }
        return id != null && id.equals(((Functionality) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Functionality{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", icon='" + getIcon() + "'" +
            ", url='" + getUrl() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
