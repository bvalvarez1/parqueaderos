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
 * The Entity catalog\n@author macf
 */
@Entity
@Table(name = "catalogue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Catalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombre de catalogo
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * codigo de catalogo-unico
     */
    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * descripcion de catalogo
     */
    @Column(name = "description")
    private String description;

    /**
     * activo
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * items de catalog
     */
    @OneToMany(mappedBy = "catalogue")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private Set<ItemCatalogue> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalogue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Catalogue name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Catalogue code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Catalogue description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Catalogue active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ItemCatalogue> getItems() {
        return this.items;
    }

    public void setItems(Set<ItemCatalogue> itemCatalogues) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCatalogue(null));
        }
        if (itemCatalogues != null) {
            itemCatalogues.forEach(i -> i.setCatalogue(this));
        }
        this.items = itemCatalogues;
    }

    public Catalogue items(Set<ItemCatalogue> itemCatalogues) {
        this.setItems(itemCatalogues);
        return this;
    }

    public Catalogue addItems(ItemCatalogue itemCatalogue) {
        this.items.add(itemCatalogue);
        itemCatalogue.setCatalogue(this);
        return this;
    }

    public Catalogue removeItems(ItemCatalogue itemCatalogue) {
        this.items.remove(itemCatalogue);
        itemCatalogue.setCatalogue(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogue)) {
            return false;
        }
        return id != null && id.equals(((Catalogue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalogue{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
