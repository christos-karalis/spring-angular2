import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES, RouteParams} from 'angular2/router';
import {AppComponent, AppDetailComponent} from './app.component';
import {LinkToComponent} from './app.directive';
import {RestService} from './services';

@Component({
    templateUrl: `main/directories/directories.html`,
    directives: [LinkToComponent]
})
class DirListComponent extends AppComponent {
    constructor(rService: RestService) {
        super(rService, 'directories');
        rService.searchEvent.subscribe((item:any) => this.loadSearch(item));
    }

    loadSearch(results:any) {
      this['directories'] = results;
      console.log(results);
    }
}

@Component({
  templateUrl:'main/directories/directories_view.html',
    directives: [LinkToComponent]
})
class DirDetailComponent extends AppDetailComponent {
    constructor(_routeParams: RouteParams, rService: RestService) {
        super(_routeParams, rService, 'directories');
    }

    ngOnInit() {
        super.ngOnInit();
        let promises = this.loadSubViews(['threads']);
        promises.unshift(this.promise);
        Promise.all(promises)
            .then(
              res => this.data['threads'] = res[1],
              err => console.log('Error loading : ' + err)
            );
    }
}

@Component({
    template: '<router-outlet></router-outlet>',
    directives: [ROUTER_DIRECTIVES]
})
@RouteConfig([
    { path: '/', component: DirListComponent, as: 'Directories', useAsDefault:true},
    { path: '/:id', component: DirDetailComponent, as: 'Directory Details' }
])
export default class DirComponent { }
