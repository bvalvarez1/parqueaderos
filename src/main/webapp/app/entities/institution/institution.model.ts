import { IPlace } from 'app/entities/place/place.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';

export interface IInstitution {
  id?: number;
  name?: string;
  address?: string | null;
  placesNumber?: number;
  ruc?: string;
  latitude?: number | null;
  longitude?: number | null;
  places?: IPlace[] | null;
  canton?: IItemCatalogue | null;
  acronym?: string | null;
  sequencename?: string;
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
    public places?: IPlace[] | null,
    public canton?: IItemCatalogue | null,
    public acronym?: string | null,
    public sequencename?: string
  ) {}
}

export function getInstitutionIdentifier(institution: IInstitution): number | undefined {
  return institution.id;
}
