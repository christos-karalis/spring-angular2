import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule  } from '@angular/router';
import { DirListComponent, DirDetailComponent,
         ThreadListComponent, ThreadDetailComponent} from './components';

const appRoutes: Routes = [
  {
    path: '', redirectTo: '/directories', pathMatch: 'full'
  },
  {
    path: 'directories', component: DirListComponent,
  },
  {
    path: 'directories/:id', component: DirDetailComponent
  },
  {
    path: 'threads', component: ThreadListComponent
  },
  {
    path: 'threads/:id', component: ThreadDetailComponent
  }];

  export const appRoutingProviders: any[] = [

  ];


export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
