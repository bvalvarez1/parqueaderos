package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Person entity.\n@author macf
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * nombres completo
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * nombre
     */
    @NotNull
    @Column(name = "fist_name", nullable = false)
    private String fistName;

    /**
     * apellido
     */
    @Column(name = "surname")
    private String surname;

    /**
     * email
     */
    @Column(name = "email")
    private String email;

    /**
     * numero identificacion
     */
    @NotNull
    @Column(name = "identificaction_number", nullable = false)
    private String identificactionNumber;

    /**
     * numero telefono convencional
     */
    @Column(name = "telephone_number")
    private String telephoneNumber;

    /**
     * usuario
     */
    @ManyToOne
    private User user;

    /**
     * tipo de identificacion
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "catalogue" }, allowSetters = true)
    private ItemCatalogue identificationType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Person fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFistName() {
        return this.fistName;
    }

    public Person fistName(String fistName) {
        this.setFistName(fistName);
        return this;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getSurname() {
        return this.surname;
    }

    public Person surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public Person email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificactionNumber() {
        return this.identificactionNumber;
    }

    public Person identificactionNumber(String identificactionNumber) {
        this.setIdentificactionNumber(identificactionNumber);
        return this;
    }

    public void setIdentificactionNumber(String identificactionNumber) {
        this.identificactionNumber = identificactionNumber;
    }

    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public Person telephoneNumber(String telephoneNumber) {
        this.setTelephoneNumber(telephoneNumber);
        return this;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Person user(User user) {
        this.setUser(user);
        return this;
    }

    public ItemCatalogue getIdentificationType() {
        return this.identificationType;
    }

    public void setIdentificationType(ItemCatalogue itemCatalogue) {
        this.identificationType = itemCatalogue;
    }

    public Person identificationType(ItemCatalogue itemCatalogue) {
        this.setIdentificationType(itemCatalogue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", fistName='" + getFistName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", identificactionNumber='" + getIdentificactionNumber() + "'" +
            ", telephoneNumber='" + getTelephoneNumber() + "'" +
            "}";
    }
}
