package ec.loja.service.dto;

import ec.loja.domain.TariffVehicleType;
import java.util.List;

public class InstitutionTarriffDTO {

    private InstitutionParkingDTO institution;
    private List<TariffVehicleType> tarrifs;

    public InstitutionParkingDTO getInstitution() {
        return institution;
    }

    public void setInstitution(InstitutionParkingDTO institution) {
        this.institution = institution;
    }

    public List<TariffVehicleType> getTarrifs() {
        return tarrifs;
    }

    public void setTarrifs(List<TariffVehicleType> tarrifs) {
        this.tarrifs = tarrifs;
    }
}
