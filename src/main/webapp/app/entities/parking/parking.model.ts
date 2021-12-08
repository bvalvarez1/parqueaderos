import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';

export interface IInstitution {
  id?: number;
  name?: string;
  address?: string | null;
  placesNumber?: number;
  ruc?: string;
  latitude?: number | null;
  longitude?: number | null;
  canton?: IItemCatalogue | null;
  sequenceName?: string | null;
  acronym?: string | null;
}

export class Institution implements IInstitution {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string | null,
    public placesNumber?: number,
    public ruc?: string,
    public latitude?: number | null,
    public longitude?: number | null,
    public canton?: IItemCatalogue | null,
    public sequenceName?: string | null,
    public acronym?: string | null
  ) {}
}

export function getInstitutionIdentifier(institution: IInstitution): number | undefined {
  return institution.id;
}
