package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Entity entity.\n@author A true hipster
 */
@Entity
@Table(name = "item_catalogue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemCatalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombre de itemcatalogo
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * codigo de itemcatalogo
     */
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * descripcion de itemcatalogo
     */
    @Column(name = "description")
    private String description;

    /**
     * codigo de catalogo
     */
    @NotNull
    @Column(name = "catalog_code", nullable = false)
    private String catalogCode;

    /**
     * activo
     */
    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Catalogue catalogue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemCatalogue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ItemCatalogue name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public ItemCatalogue code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public ItemCatalogue description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatalogCode() {
        return this.catalogCode;
    }

    public ItemCatalogue catalogCode(String catalogCode) {
        this.setCatalogCode(catalogCode);
        return this;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ItemCatalogue active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Catalogue getCatalogue() {
        return this.catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public ItemCatalogue catalogue(Catalogue catalogue) {
        this.setCatalogue(catalogue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemCatalogue)) {
            return false;
        }
        return id != null && id.equals(((ItemCatalogue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCatalogue{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", catalogCode='" + getCatalogCode() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
