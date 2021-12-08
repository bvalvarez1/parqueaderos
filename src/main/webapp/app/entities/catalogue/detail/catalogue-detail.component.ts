import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogue } from '../catalogue.model';

@Component({
  selector: 'jhi-catalogue-detail',
  templateUrl: './catalogue-detail.component.html',
})
export class CatalogueDetailComponent implements OnInit {
  catalogue: ICatalogue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogue }) => {
      this.catalogue = catalogue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
