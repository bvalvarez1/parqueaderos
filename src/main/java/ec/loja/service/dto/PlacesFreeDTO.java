package ec.loja.service.dto;

import java.math.BigDecimal;

public interface PlacesFreeDTO {
    Long getId();

    String getName();

    String getSequencename();

    BigDecimal getLatitude();

    BigDecimal getLongitude();

    Integer getAvailables();
}
