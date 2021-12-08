package ec.loja.service.dto;

import java.io.Serializable;

public class RecordTicketRequest implements Serializable {

    private String sequential;
    private Long institutionid;

    public String getSequential() {
        return sequential;
    }

    public void setSequential(String sequential) {
        this.sequential = sequential;
    }

    public Long getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(Long institutionid) {
        this.institutionid = institutionid;
    }
}
