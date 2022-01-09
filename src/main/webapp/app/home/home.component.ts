import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';
import { IFreePlaces } from 'app/entities/freePlaces/freeplaces.model';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ReserveDialogComponent } from './reserve/reserve-dialog.component';
@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  google: any;
  lat: any;
  lng: any;
  freePlaces: IFreePlaces[] | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private institutionService: InstitutionService,
    private router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.getLocation();
  }

  /**
   * Anular un ticket
   */
  preReserve(id: number): void {
    const modalRef = this.modalService.open(ReserveDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.institutionId = id;

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'prereserved') {
        //this.loadData();
        // eslint-disable-next-line no-console
        console.log('PRE RESERVADO');
      }
    });
  }

  getLocation(): void {
    //if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      position => {
        //if (position) {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;

        // eslint-disable-next-line no-console
        console.log('-00-0-0-0-0-');
        // eslint-disable-next-line no-console
        console.log(this.lat);
        // eslint-disable-next-line no-console
        console.log(this.lng);

        this.institutionService.getNearSuggestions(this.lat, this.lng).subscribe(
          (res: HttpResponse<IFreePlaces[]>) => {
            if (res.body != null) {
              this.freePlaces = res.body;
            }
          },
          error => {
            // eslint-disable-next-line no-console
            console.log(error);
          }
        );
        //}
      },
      error =>
        // eslint-disable-next-line no-console
        console.log(error)
    );
    //} else {
    //  alert("Geolocation is not supported by this browser.");
    //}
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
