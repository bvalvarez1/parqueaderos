import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHorary } from '../horary.model';

@Component({
  selector: 'jhi-horary-detail',
  templateUrl: './horary-detail.component.html',
})
export class HoraryDetailComponent implements OnInit {
  horary: IHorary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horary }) => {
      this.horary = horary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
