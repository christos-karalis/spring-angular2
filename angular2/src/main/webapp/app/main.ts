import {bootstrap} from 'angular2/platform/browser';
import {AppRouterComponent} from './app.router';
import {UserService, RestService} from './services';
import {HTTP_PROVIDERS} from 'angular2/http';
import {ROUTER_PROVIDERS} from 'angular2/router';
import {provide} from 'angular2/core';
import {LocationStrategy, HashLocationStrategy} from 'angular2/platform/common';

//enableProdMode();

bootstrap(AppRouterComponent, [HTTP_PROVIDERS, ROUTER_PROVIDERS, RestService, UserService, provide(LocationStrategy,
         {useClass: HashLocationStrategy})]);
