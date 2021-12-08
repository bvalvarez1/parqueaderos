import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HoraryComponent } from './list/horary.component';
import { HoraryDetailComponent } from './detail/horary-detail.component';
import { HoraryUpdateComponent } from './update/horary-update.component';
import { HoraryDeleteDialogComponent } from './delete/horary-delete-dialog.component';
import { HoraryRoutingModule } from './route/horary-routing.module';

@NgModule({
  imports: [SharedModule, HoraryRoutingModule],
  declarations: [HoraryComponent, HoraryDetailComponent, HoraryUpdateComponent, HoraryDeleteDialogComponent],
  entryComponents: [HoraryDeleteDialogComponent],
})
export class HoraryModule {}
