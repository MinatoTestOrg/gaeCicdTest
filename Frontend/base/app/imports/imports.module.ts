import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ImportsRoutingModule } from './imports-routing.module';
import { ImportPageComponent } from './import-page/import-page.component';
// import { ImportPageComponent } from './import-page/import-page.component';
import { WidgetsBaseModule } from '@baseapp/widgets/widgets.base.module';
import { SharedModule } from '@app/shared/shared.module';
import { ImportErrorDetailsComponent } from './import-error-details/import-error-details.component';
import { ImportHistoryComponent } from './import-history/import-history/import-history.component';


@NgModule({
  declarations: [
    ImportPageComponent,
    ImportErrorDetailsComponent,
    ImportHistoryComponent
    // ImportPageComponent
  ],
  imports: [
    CommonModule,
    WidgetsBaseModule,
    SharedModule,
    ImportsRoutingModule
  ],
  exports:[
    SharedModule, ImportPageComponent
  ]
})
export class ImportsModule { }
