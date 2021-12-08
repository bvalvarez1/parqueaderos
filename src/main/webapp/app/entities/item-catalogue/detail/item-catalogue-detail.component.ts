import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemCatalogue } from '../item-catalogue.model';

@Component({
  selector: 'jhi-item-catalogue-detail',
  templateUrl: './item-catalogue-detail.component.html',
})
export class ItemCatalogueDetailComponent implements OnInit {
  itemCatalogue: IItemCatalogue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemCatalogue }) => {
      this.itemCatalogue = itemCatalogue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
