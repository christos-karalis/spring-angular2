import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule, AppRouterComponent} from './app.router';
import {UserService, RestService} from './services';
import {DirListComponent, DirDetailComponent } from './components';

//enableProdMode();

const platform = platformBrowserDynamic();
platform.bootstrapModule(AppModule);
