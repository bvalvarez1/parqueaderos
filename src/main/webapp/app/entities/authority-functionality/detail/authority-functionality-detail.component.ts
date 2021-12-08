import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuthorityFunctionality } from '../authority-functionality.model';

@Component({
  selector: 'jhi-authority-functionality-detail',
  templateUrl: './authority-functionality-detail.component.html',
})
export class AuthorityFunctionalityDetailComponent implements OnInit {
  authorityFunctionality: IAuthorityFunctionality | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authorityFunctionality }) => {
      this.authorityFunctionality = authorityFunctionality;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
