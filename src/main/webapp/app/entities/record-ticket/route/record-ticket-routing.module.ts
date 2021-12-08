import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecordTicketComponent } from '../list/record-ticket.component';
import { RecordTicketDetailComponent } from '../detail/record-ticket-detail.component';
import { RecordTicketUpdateComponent } from '../update/record-ticket-update.component';
import { RecordTicketRoutingResolveService } from './record-ticket-routing-resolve.service';

const recordTicketRoute: Routes = [
  {
    path: '',
    component: RecordTicketComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecordTicketDetailComponent,
    resolve: {
      recordTicket: RecordTicketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecordTicketUpdateComponent,
    resolve: {
      recordTicket: RecordTicketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecordTicketUpdateComponent,
    resolve: {
      recordTicket: RecordTicketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recordTicketRoute)],
  exports: [RouterModule],
})
export class RecordTicketRoutingModule {}
