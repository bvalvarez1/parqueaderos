import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ITariffVehicleType } from '../tariff-vehicle-type/tariff-vehicle-type.model';
import { IInstitution } from './parking.model';

export interface IInstitutionTarrif {
  institution?: IInstitution;
  tarrifs?: ITariffVehicleType[];
}

export class InstitutionTarrif implements IInstitutionTarrif {
  constructor(public institution?: IInstitution, public tarrifs?: ITariffVehicleType[]) {}
}

export function getInstitutionIdentifier(institution: IInstitution): number | undefined {
  return institution.id;
}
