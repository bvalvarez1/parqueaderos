package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Place entity.\n@author A true hipster
 */
@Entity
@Table(name = "place")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * number
     */
    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    /**
     * estado del lugar de estacionamiento
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "places", "canton" }, allowSetters = true)
    private Institution institution;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Place id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public Place number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ItemCatalogue getStatus() {
        return this.status;
    }

    public void setStatus(ItemCatalogue itemCatalogue) {
        this.status = itemCatalogue;
    }

    public Place status(ItemCatalogue itemCatalogue) {
        this.setStatus(itemCatalogue);
        return this;
    }

    public Institution getInstitution() {
        return this.institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Place institution(Institution institution) {
        this.setInstitution(institution);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Place)) {
            return false;
        }
        return id != null && id.equals(((Place) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Place{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
