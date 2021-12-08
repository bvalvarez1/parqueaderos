package ec.loja.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ec.loja.domain.Receipt} entity.
 */
@ApiModel(description = "The Receipt entity.\n@author A true hipster")
public class ReceiptDTO implements Serializable {

    private Long id;

    /**
     * autorizacion del SRI
     */
    @ApiModelProperty(value = "autorizacion del SRI")
    private String authorizationNumber;

    /**
     * secuencia de la factura
     */
    @ApiModelProperty(value = "secuencia de la factura")
    private String sequential;

    /**
     * estado de la factura
     */
    @ApiModelProperty(value = "estado de la factura")
    private String status;

    /**
     * clave de acceso sri
     */
    @ApiModelProperty(value = "clave de acceso sri")
    private String sriaccesskey;

    /**
     * fecha autorizacion sri
     */
    @ApiModelProperty(value = "fecha autorizacion sri")
    private LocalDate sriauthorizationdate;

    /**
     * fecha emision factura
     */
    @ApiModelProperty(value = "fecha emision factura")
    private LocalDate receiptdate;

    private RecordTicketDTO recordticketid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public String getSequential() {
        return sequential;
    }

    public void setSequential(String sequential) {
        this.sequential = sequential;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSriaccesskey() {
        return sriaccesskey;
    }

    public void setSriaccesskey(String sriaccesskey) {
        this.sriaccesskey = sriaccesskey;
    }

    public LocalDate getSriauthorizationdate() {
        return sriauthorizationdate;
    }

    public void setSriauthorizationdate(LocalDate sriauthorizationdate) {
        this.sriauthorizationdate = sriauthorizationdate;
    }

    public LocalDate getReceiptdate() {
        return receiptdate;
    }

    public void setReceiptdate(LocalDate receiptdate) {
        this.receiptdate = receiptdate;
    }

    public RecordTicketDTO getRecordticketid() {
        return recordticketid;
    }

    public void setRecordticketid(RecordTicketDTO recordticketid) {
        this.recordticketid = recordticketid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceiptDTO)) {
            return false;
        }

        ReceiptDTO receiptDTO = (ReceiptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receiptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceiptDTO{" +
            "id=" + getId() +
            ", authorizationNumber='" + getAuthorizationNumber() + "'" +
            ", sequential='" + getSequential() + "'" +
            ", status='" + getStatus() + "'" +
            ", sriaccesskey='" + getSriaccesskey() + "'" +
            ", sriauthorizationdate='" + getSriauthorizationdate() + "'" +
            ", receiptdate='" + getReceiptdate() + "'" +
            ", recordticketid=" + getRecordticketid() +
            "}";
    }
}
