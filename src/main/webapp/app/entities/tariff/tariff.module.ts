import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TariffComponent } from './list/tariff.component';
import { TariffDetailComponent } from './detail/tariff-detail.component';
import { TariffUpdateComponent } from './update/tariff-update.component';
import { TariffDeleteDialogComponent } from './delete/tariff-delete-dialog.component';
import { TariffRoutingModule } from './route/tariff-routing.module';

@NgModule({
  imports: [SharedModule, TariffRoutingModule],
  declarations: [TariffComponent, TariffDetailComponent, TariffUpdateComponent, TariffDeleteDialogComponent],
  entryComponents: [TariffDeleteDialogComponent],
})
export class TariffModule {}
