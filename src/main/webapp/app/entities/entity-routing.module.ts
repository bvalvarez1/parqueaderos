import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'catalogue',
        data: { pageTitle: 'parqueaderosApp.catalogue.home.title' },
        loadChildren: () => import('./catalogue/catalogue.module').then(m => m.CatalogueModule),
      },
      {
        path: 'item-catalogue',
        data: { pageTitle: 'parqueaderosApp.itemCatalogue.home.title' },
        loadChildren: () => import('./item-catalogue/item-catalogue.module').then(m => m.ItemCatalogueModule),
      },
      {
        path: 'institution',
        data: { pageTitle: 'parqueaderosApp.institution.home.title' },
        loadChildren: () => import('./institution/institution.module').then(m => m.InstitutionModule),
      },
      {
        path: 'system-parameters',
        data: { pageTitle: 'parqueaderosApp.systemParameters.home.title' },
        loadChildren: () => import('./system-parameters/system-parameters.module').then(m => m.SystemParametersModule),
      },
      {
        path: 'system-parameter-institution',
        data: { pageTitle: 'parqueaderosApp.systemParameterInstitution.home.title' },
        loadChildren: () =>
          import('./system-parameter-institution/system-parameter-institution.module').then(m => m.SystemParameterInstitutionModule),
      },
      {
        path: 'person',
        data: { pageTitle: 'parqueaderosApp.person.home.title' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'functionality',
        data: { pageTitle: 'parqueaderosApp.functionality.home.title' },
        loadChildren: () => import('./functionality/functionality.module').then(m => m.FunctionalityModule),
      },
      {
        path: 'authority-functionality',
        data: { pageTitle: 'parqueaderosApp.authorityFunctionality.home.title' },
        loadChildren: () => import('./authority-functionality/authority-functionality.module').then(m => m.AuthorityFunctionalityModule),
      },
      {
        path: 'contract',
        data: { pageTitle: 'parqueaderosApp.contract.home.title' },
        loadChildren: () => import('./contract/contract.module').then(m => m.ContractModule),
      },
      {
        path: 'horary',
        data: { pageTitle: 'parqueaderosApp.horary.home.title' },
        loadChildren: () => import('./horary/horary.module').then(m => m.HoraryModule),
      },
      {
        path: 'record',
        data: { pageTitle: 'parqueaderosApp.record.home.title' },
        loadChildren: () => import('./record/record.module').then(m => m.RecordModule),
      },
      {
        path: 'tariff',
        data: { pageTitle: 'parqueaderosApp.tariff.home.title' },
        loadChildren: () => import('./tariff/tariff.module').then(m => m.TariffModule),
      },
      {
        path: 'tariff-vehicle-type',
        data: { pageTitle: 'parqueaderosApp.tariffVehicleType.home.title' },
        loadChildren: () => import('./tariff-vehicle-type/tariff-vehicle-type.module').then(m => m.TariffVehicleTypeModule),
      },
      {
        path: 'user-authority',
        data: { pageTitle: 'parqueaderosApp.userAuthority.home.title' },
        loadChildren: () => import('./user-authority/user-authority.module').then(m => m.UserAuthorityModule),
      },
      {
        path: 'user-authority-institution',
        data: { pageTitle: 'parqueaderosApp.userAuthorityInstitution.home.title' },
        loadChildren: () =>
          import('./user-authority-institution/user-authority-institution.module').then(m => m.UserAuthorityInstitutionModule),
      },
      {
        path: 'receipt',
        data: { pageTitle: 'parqueaderosApp.receipt.home.title' },
        loadChildren: () => import('./receipt/receipt.module').then(m => m.ReceiptModule),
      },
      {
        path: 'record-ticket',
        data: { pageTitle: 'parqueaderosApp.recordTicket.home.title' },
        loadChildren: () => import('./record-ticket/record-ticket.module').then(m => m.RecordTicketModule),
      },
      {
        path: 'parking',
        data: { pageTitle: 'parqueaderosApp.parking.home.title' },
        loadChildren: () => import('./parking/parking.module').then(m => m.ParkingModule),
      },
      {
        path: 'place',
        data: { pageTitle: 'parqueaderosApp.place.home.title' },
        loadChildren: () => import('./place/place.module').then(m => m.PlaceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
