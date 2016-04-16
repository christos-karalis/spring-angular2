import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import {RouteParams} from 'angular2/router';
import {RestService} from './rest.service';
import {AppComponent, AppDetailComponent} from './app.component';
import {LinkToComponent} from './app.directive';

@Component({
  templateUrl:`main/threads/threads.html`,
  directives: [LinkToComponent]
})
export class ThreadListComponent extends AppComponent {
  constructor(rService : RestService) {
    super(rService, 'threads'); }
}

@Component({
  templateUrl:`main/threads/threads_view.html`,
  directives: [LinkToComponent]
})
class ThreadDetailComponent extends AppDetailComponent {

  constructor(_routeParams: RouteParams, rService : RestService) {
    super(_routeParams, rService, 'threads');
  }

  ngOnInit() {
    super.ngOnInit();
    let promises = this.loadSubViews(['posts']);
    promises.unshift(this.promise);
    Promise.all(promises)
           .then(res => this.data['posts'] = res[1],
                 err => console.log('Error loading : ' + err)
                );
  }
}

@Component({
  template:` <router-outlet></router-outlet> `,
  directives: [ROUTER_DIRECTIVES]
})
@RouteConfig([
  {path:'/', component: ThreadListComponent, useAsDefault:true},
  {path:'/:id', component: ThreadDetailComponent}
])
export default class ThreadComponent {}
