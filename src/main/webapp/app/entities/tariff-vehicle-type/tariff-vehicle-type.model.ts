import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ITariff } from 'app/entities/tariff/tariff.model';

export interface ITariffVehicleType {
  id?: number;
  minValue?: number;
  maxValue?: number;
  value?: number;
  vehicleType?: IItemCatalogue;
  tariff?: ITariff | null;
}

export class TariffVehicleType implements ITariffVehicleType {
  constructor(
    public id?: number,
    public minValue?: number,
    public maxValue?: number,
    public value?: number,
    public vehicleType?: IItemCatalogue,
    public tariff?: ITariff | null
  ) {}
}

export function getTariffVehicleTypeIdentifier(tariffVehicleType: ITariffVehicleType): number | undefined {
  return tariffVehicleType.id;
}
