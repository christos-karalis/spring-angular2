import {Component, DynamicComponentLoader, Injector, OnInit} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import DirComponent from './dir.component';
import ThreadComponent from './thread.component';
import {RestService} from './rest.service';
import {LinkToComponent, InfoBoxComponent} from './app.directive';

@Component({
  selector: 'main-page',
  templateUrl:  `main/common/trend.html`,
  directives: [ROUTER_DIRECTIVES, InfoBoxComponent]
})
@RouteConfig([
  {path:'/directories/...', component: DirComponent, as: 'Directory Pages', useAsDefault:true},
  {path:'/threads/...', component: ThreadComponent, as: 'Thread Pages'}
])
export class AppRouterComponent {
}
