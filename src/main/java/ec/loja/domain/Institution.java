package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Institution entity.\n@author macf
 */
@Entity
@Table(name = "institution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Institution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombre de la empresa/institucion
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Direccion
     */
    @Column(name = "address")
    private String address;

    /**
     * espacios disponibles
     */
    @NotNull
    @Column(name = "places_number", nullable = false)
    private Integer placesNumber;

    /**
     * ruc de la empresa
     */
    @NotNull
    @Column(name = "ruc", nullable = false)
    private String ruc;

    /**
     * latitud
     */
    @Column(name = "latitude", precision = 21, scale = 2)
    private BigDecimal latitude;

    /**
     * longitud
     */
    @Column(name = "longitude", precision = 21, scale = 2)
    private BigDecimal longitude;

    /**
     * for sequential
     */
    @NotNull
    @Column(name = "acronym", nullable = false)
    private String acronym;

    /**
     * for sequential
     */
    @NotNull
    @Column(name = "sequencename")
    private String sequencename;

    /**
     * places of institution
     */
    @OneToMany(mappedBy = "institution")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "status", "institution" }, allowSetters = true)
    private Set<Place> places = new HashSet<>();

    /**
     * canton del parqueadero
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue canton;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Institution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Institution name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Institution address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPlacesNumber() {
        return this.placesNumber;
    }

    public Institution placesNumber(Integer placesNumber) {
        this.setPlacesNumber(placesNumber);
        return this;
    }

    public void setPlacesNumber(Integer placesNumber) {
        this.placesNumber = placesNumber;
    }

    public String getRuc() {
        return this.ruc;
    }

    public Institution ruc(String ruc) {
        this.setRuc(ruc);
        return this;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public Institution latitude(BigDecimal latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public Institution longitude(BigDecimal longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public Institution acronym(String acronym) {
        this.setAcronym(acronym);
        return this;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getSequencename() {
        return this.sequencename;
    }

    public Institution sequencename(String sequencename) {
        this.setSequencename(sequencename);
        return this;
    }

    public void setSequencename(String sequencename) {
        this.sequencename = sequencename;
    }

    public Set<Place> getPlaces() {
        return this.places;
    }

    public void setPlaces(Set<Place> places) {
        if (this.places != null) {
            this.places.forEach(i -> i.setInstitution(null));
        }
        if (places != null) {
            places.forEach(i -> i.setInstitution(this));
        }
        this.places = places;
    }

    public Institution places(Set<Place> places) {
        this.setPlaces(places);
        return this;
    }

    public Institution addPlaces(Place place) {
        this.places.add(place);
        place.setInstitution(this);
        return this;
    }

    public Institution removePlaces(Place place) {
        this.places.remove(place);
        place.setInstitution(null);
        return this;
    }

    public ItemCatalogue getCanton() {
        return this.canton;
    }

    public void setCanton(ItemCatalogue itemCatalogue) {
        this.canton = itemCatalogue;
    }

    public Institution canton(ItemCatalogue itemCatalogue) {
        this.setCanton(itemCatalogue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Institution)) {
            return false;
        }
        return id != null && id.equals(((Institution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Institution{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", placesNumber=" + getPlacesNumber() +
            ", ruc='" + getRuc() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", acronym='" + getAcronym() + "'" +
            ", sequencename='" + getSequencename() + "'" +
            "}";
    }
}
