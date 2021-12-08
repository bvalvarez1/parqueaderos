import * as dayjs from 'dayjs';
import { IPlace } from 'app/entities/place/place.model';
import { IUser } from 'app/entities/user/user.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ITariffVehicleType } from 'app/entities/tariff-vehicle-type/tariff-vehicle-type.model';
import { IInstitution } from '../institution/institution.model';

export interface IRecordTicket {
  id?: number;
  initDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  plate?: string | null;
  parkingTime?: dayjs.Dayjs | null;
  taxableTotal?: number | null;
  taxes?: number | null;
  total?: number | null;
  observation?: string | null;
  sequential?: string | null;
  placeid?: IPlace | null;
  emitter?: IUser | null;
  collector?: IUser | null;
  status?: IItemCatalogue;
  tariffVehicle?: ITariffVehicleType;
  institution?: IInstitution;
  days?: number | null;
  hours?: number | null;
  minutes?: number | null;
  seconds?: number | null;
}

export class RecordTicket implements IRecordTicket {
  constructor(
    public id?: number,
    public initDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs | null,
    public plate?: string | null,
    public parkingTime?: dayjs.Dayjs | null,
    public taxableTotal?: number | null,
    public taxes?: number | null,
    public total?: number | null,
    public observation?: string | null,
    public sequential?: string | null,
    public placeid?: IPlace | null,
    public emitter?: IUser | null,
    public collector?: IUser | null,
    public status?: IItemCatalogue,
    public tariffVehicle?: ITariffVehicleType,
    public institution?: IInstitution,
    public days?: number | null,
    public hours?: number | null,
    public minutes?: number | null,
    public seconds?: number | null
  ) {}
}

export function getRecordTicketIdentifier(recordTicket: IRecordTicket): number | undefined {
  return recordTicket.id;
}
