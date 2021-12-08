import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReceipt, Receipt } from '../receipt.model';
import { ReceiptService } from '../service/receipt.service';

@Injectable({ providedIn: 'root' })
export class ReceiptRoutingResolveService implements Resolve<IReceipt> {
  constructor(protected service: ReceiptService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReceipt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((receipt: HttpResponse<Receipt>) => {
          if (receipt.body) {
            return of(receipt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Receipt());
  }
}
