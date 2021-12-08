import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecordTicket, RecordTicket } from '../record-ticket.model';
import { RecordTicketService } from '../service/record-ticket.service';

@Injectable({ providedIn: 'root' })
export class RecordTicketRoutingResolveService implements Resolve<IRecordTicket> {
  constructor(protected service: RecordTicketService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecordTicket> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recordTicket: HttpResponse<RecordTicket>) => {
          if (recordTicket.body) {
            return of(recordTicket.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecordTicket());
  }
}
