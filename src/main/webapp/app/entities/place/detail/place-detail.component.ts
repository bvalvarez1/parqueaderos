import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlace } from '../place.model';

@Component({
  selector: 'jhi-place-detail',
  templateUrl: './place-detail.component.html',
})
export class PlaceDetailComponent implements OnInit {
  place: IPlace | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ place }) => {
      this.place = place;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
