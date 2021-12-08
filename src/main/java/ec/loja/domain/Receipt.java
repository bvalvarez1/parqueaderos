package ec.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Receipt entity.\n@author A true hipster
 */
@Entity
@Table(name = "receipt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * autorizacion del SRI
     */
    @Column(name = "authorization_number")
    private String authorizationNumber;

    /**
     * secuencia de la factura
     */
    @Column(name = "sequential")
    private String sequential;

    /**
     * estado de la factura
     */
    @Column(name = "status")
    private String status;

    /**
     * clave de acceso sri
     */
    @Column(name = "sriaccesskey")
    private String sriaccesskey;

    /**
     * fecha autorizacion sri
     */
    @Column(name = "sriauthorizationdate")
    private LocalDate sriauthorizationdate;

    /**
     * fecha emision factura
     */
    @Column(name = "receiptdate")
    private LocalDate receiptdate;

    @JsonIgnoreProperties(value = { "placeid", "emitter", "collector", "status", "tariffVehicle", "institution" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private RecordTicket recordticketid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Receipt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizationNumber() {
        return this.authorizationNumber;
    }

    public Receipt authorizationNumber(String authorizationNumber) {
        this.setAuthorizationNumber(authorizationNumber);
        return this;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public String getSequential() {
        return this.sequential;
    }

    public Receipt sequential(String sequential) {
        this.setSequential(sequential);
        return this;
    }

    public void setSequential(String sequential) {
        this.sequential = sequential;
    }

    public String getStatus() {
        return this.status;
    }

    public Receipt status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSriaccesskey() {
        return this.sriaccesskey;
    }

    public Receipt sriaccesskey(String sriaccesskey) {
        this.setSriaccesskey(sriaccesskey);
        return this;
    }

    public void setSriaccesskey(String sriaccesskey) {
        this.sriaccesskey = sriaccesskey;
    }

    public LocalDate getSriauthorizationdate() {
        return this.sriauthorizationdate;
    }

    public Receipt sriauthorizationdate(LocalDate sriauthorizationdate) {
        this.setSriauthorizationdate(sriauthorizationdate);
        return this;
    }

    public void setSriauthorizationdate(LocalDate sriauthorizationdate) {
        this.sriauthorizationdate = sriauthorizationdate;
    }

    public LocalDate getReceiptdate() {
        return this.receiptdate;
    }

    public Receipt receiptdate(LocalDate receiptdate) {
        this.setReceiptdate(receiptdate);
        return this;
    }

    public void setReceiptdate(LocalDate receiptdate) {
        this.receiptdate = receiptdate;
    }

    public RecordTicket getRecordticketid() {
        return this.recordticketid;
    }

    public void setRecordticketid(RecordTicket recordTicket) {
        this.recordticketid = recordTicket;
    }

    public Receipt recordticketid(RecordTicket recordTicket) {
        this.setRecordticketid(recordTicket);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receipt)) {
            return false;
        }
        return id != null && id.equals(((Receipt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + getId() +
            ", authorizationNumber='" + getAuthorizationNumber() + "'" +
            ", sequential='" + getSequential() + "'" +
            ", status='" + getStatus() + "'" +
            ", sriaccesskey='" + getSriaccesskey() + "'" +
            ", sriauthorizationdate='" + getSriauthorizationdate() + "'" +
            ", receiptdate='" + getReceiptdate() + "'" +
            "}";
    }
}
