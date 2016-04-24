import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES, RouteParams} from 'angular2/router';
import {RestService} from './rest.service';
import {AppComponent, AppDetailComponent} from './app.component';
import {LinkToComponent} from './app.directive';

@Component({
    templateUrl: `main/directories/directories.html`,
    directives: [LinkToComponent]
})
class DirListComponent extends AppComponent {
    constructor(rService: RestService) {
        super(rService, 'directories');
    }
}

@Component({
    template: `
                <p>{{data?.title}}</p>
                <div>
                    <div class="post"  *ngFor="#thread of data?.threads">
                      <div class="wrap-ut pull-left">
                          <div class="userinfo pull-left">
                          </div>
                          <div class="posttext pull-left">
                              <h2>
                                <link-to [path]="'threads'" [label]="thread?.title" [id]="thread?.id"></link-to>
                              </h2>
                              <p>{{thread?.title}}</p>
                          </div>
                          <div class="clearfix"></div>
                          <button (click)="processWithinAngularZone()">TESTTEST</button>
                      </div>
                      <div class="clearfix"></div>
                  </div>
                </div>
              `,
    directives: [LinkToComponent]
})
class DirDetailComponent extends AppDetailComponent {
    constructor(_routeParams: RouteParams, rService: RestService) {
        super(_routeParams, rService, 'directories');
    }

    processWithinAngularZone() {
      console.log('test');
      this.data['threads'][0].title = 'shit';
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
    template: ` <router-outlet></router-outlet>`,
    directives: [ROUTER_DIRECTIVES]
})
@RouteConfig([
    { path: '/', component: DirListComponent, as: 'Directories', useAsDefault:true},
    { path: '/:id', component: DirDetailComponent, as: 'Directory Details' }
])
export default class DirComponent { }
