import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TariffVehicleTypeComponent } from './list/tariff-vehicle-type.component';
import { TariffVehicleTypeDetailComponent } from './detail/tariff-vehicle-type-detail.component';
import { TariffVehicleTypeUpdateComponent } from './update/tariff-vehicle-type-update.component';
import { TariffVehicleTypeDeleteDialogComponent } from './delete/tariff-vehicle-type-delete-dialog.component';
import { TariffVehicleTypeRoutingModule } from './route/tariff-vehicle-type-routing.module';

@NgModule({
  imports: [SharedModule, TariffVehicleTypeRoutingModule],
  declarations: [
    TariffVehicleTypeComponent,
    TariffVehicleTypeDetailComponent,
    TariffVehicleTypeUpdateComponent,
    TariffVehicleTypeDeleteDialogComponent,
  ],
  entryComponents: [TariffVehicleTypeDeleteDialogComponent],
})
export class TariffVehicleTypeModule {}
