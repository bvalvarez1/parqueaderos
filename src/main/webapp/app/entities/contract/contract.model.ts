import * as dayjs from 'dayjs';
import { IHorary } from 'app/entities/horary/horary.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IContract {
  id?: number;
  number?: string;
  initDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  value?: number;
  hours?: number | null;
  horaries?: IHorary[] | null;
  status?: IItemCatalogue;
  contractor?: IPerson;
}

export class Contract implements IContract {
  constructor(
    public id?: number,
    public number?: string,
    public initDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public value?: number,
    public hours?: number | null,
    public horaries?: IHorary[] | null,
    public status?: IItemCatalogue,
    public contractor?: IPerson
  ) {}
}

export function getContractIdentifier(contract: IContract): number | undefined {
  return contract.id;
}
