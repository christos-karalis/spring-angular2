import {Component, Type, EventEmitter} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import DirComponent from './dir.component';
import ThreadComponent from './thread.component';
import {PopoverDirective} from './popover.directive'
import {LoginComponent} from './login.component'
import {InfoBoxComponent} from './app.directive';
import {UserService, RestService} from './services';

@Component({
  selector: 'header-login',
  templateUrl: 'main/common/header_login.html',
  directives: [PopoverDirective]
})
export class HeaderLoginComponent {
  private loginComponent:Type = LoginComponent;

  constructor(private userService:UserService) {}

  logout() { this.userService.logout(); }
  isAuthenticated() { return this.userService.isAuthenticated(); }
}

@Component({
  selector: 'header-search',
  templateUrl: 'main/common/header_search.html'
})
export class HeaderSearchComponent {
  public searchEvent: EventEmitter<any> = new EventEmitter<any>();
  private searchObject = {'operator': 'OR', 'operands' : {}}
  private searchText : string;

  constructor(private restService:RestService) {}

  public search() {
    this.searchObject.operands={ title : this.searchText, description: this.searchText}
    this.restService.search('directories', this.searchObject);
  }
}

@Component({
  selector: 'main-page',
  templateUrl:  `main/common/trend.html`,
  directives: [ROUTER_DIRECTIVES, InfoBoxComponent, HeaderLoginComponent, HeaderSearchComponent]
})
@RouteConfig([
  {path:'/directories/...', component: DirComponent, as: 'Directories', useAsDefault:true},
  {path:'/threads/...', component: ThreadComponent, as: 'Threads'}
])
export class AppRouterComponent {}
