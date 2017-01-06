import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule, AppRouterComponent} from './app.router';
import {UserService, RestService} from './services';
import { DirListComponent, DirDetailComponent } from './dir.component';

//enableProdMode();

const platform = platformBrowserDynamic();
platform.bootstrapModule(AppModule);
