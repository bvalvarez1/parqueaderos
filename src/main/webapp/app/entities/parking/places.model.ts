import * as dayjs from 'dayjs';

export interface IPlaces {
  placeid_?: number;
  ticketid_?: number;
  number_?: string;
  placeStatus_?: string | null;
  placeCode_?: string | null;
  ticketStatus_?: string | null;
  ticketCode_?: string | null;
  sequential_?: string | null;
  initDate_?: dayjs.Dayjs | null;
  endDate_?: dayjs.Dayjs | null;
}

export class Places implements IPlaces {
  constructor(
    public placeid_?: number,
    public ticketid_?: number,
    public number_?: string,
    public placeStatus_?: string | null,
    public placeCode_?: string | null,
    public ticketStatus_?: string | null,
    public ticketCode_?: string | null,
    public sequential_?: string | null,
    public initDate_?: dayjs.Dayjs | null,
    public endDate_?: dayjs.Dayjs | null
  ) {}
}

export function getPlaceTicketIdentifier(placeTicket: IPlaces): number | undefined {
  return placeTicket.placeid_;
}
