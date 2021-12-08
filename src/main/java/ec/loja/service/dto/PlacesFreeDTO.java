package ec.loja.service.dto;

import java.math.BigDecimal;

public interface PlacesFreeDTO {
    Long getPlaceId();

    String getName();

    String getSequencename();

    BigDecimal getLatitude();

    BigDecimal getLongitude();

    Integer getAvailables();
}
