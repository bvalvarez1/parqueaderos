package ec.loja.service.dto;

import java.util.Date;

public interface PlaceTicketsDTO {
    Long getPlaceId_();

    Long getTicketId_();

    String getNumber_();

    String getPlaceStatus_();

    String getPlaceCode_();

    String getTicketStatus_();

    String getTicketCode_();

    String getSequential_();

    Date getInitDate_();

    Date getEndDate_();
}
