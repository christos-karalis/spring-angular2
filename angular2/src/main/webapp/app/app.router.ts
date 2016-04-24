import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import DirComponent from './dir.component';
import ThreadComponent from './thread.component';
import {InfoBoxComponent} from './app.directive';

@Component({
  selector: 'main-page',
  templateUrl:  `main/common/trend.html`,
  directives: [ROUTER_DIRECTIVES, InfoBoxComponent]
})
@RouteConfig([
  {path:'/directories/...', component: DirComponent, useAsDefault:true},
  {path:'/threads/...', component: ThreadComponent}
])
export class AppRouterComponent {
}
