import { RouterModule, Routes } from '@angular/router';
import { AppLayoutComponent } from '@app/app-layout/app-layout.component';

export const routes: Routes = [{
  path: 'auth',
  children: [
    {
      path: '',
      loadChildren: () => import('@app/auth/auth.module').then(m => m.AuthModule)
    },
  ]
},
{
  path: '',
  component: AppLayoutComponent,  
  children: []
}];

