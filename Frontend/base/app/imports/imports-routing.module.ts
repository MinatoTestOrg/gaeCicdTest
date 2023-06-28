import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CanDeactivateGuard } from '@baseapp/auth.can-deactivate-guard.service';

import { ImportPageComponent } from './import-page/import-page.component';
import { ImportHistoryComponent } from './import-history/import-history/import-history.component';

const routes: Routes = [
  {
       path: 'import',
       component: ImportPageComponent,
       canDeactivate: [ CanDeactivateGuard ],
       data: {
         label: "Imports page",
          breadcrumb: "IP",
          roles : [
                "Development Administrator"
          ]
       }
  },
  {
       path: 'importspage',
       component: ImportHistoryComponent,
       canDeactivate: [ CanDeactivateGuard ],
       data: {
         label: "Imports Page",
          breadcrumb: "IH",
          roles : [
                "Development Administrator"
          ]
       }
  }
  ];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ImportsRoutingModule { }
