import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {OnDestroy} from '@angular/core';

import {AppComponent, AppDetailComponent} from './app.component';
import {RestService, UserService} from './services';
import {Subscription} from 'rxjs/Rx';


@Component({
  templateUrl: `main/directories/directories.html`
})
export class DirListComponent extends AppComponent {
    subscription : Subscription;

    constructor(rService: RestService, userService : UserService) {
      super(rService, userService, 'directories');
      this.subscription = rService.searchEvent.subscribe((item:any) => this.loadSearch(item));
    }

    ngOnDestroy() {
      console.log('Destroying Directory Component');
      this.subscription.unsubscribe();
    }

    loadSearch(results:any) {
      this['directories'] = results;
    }
}

@Component({
  templateUrl:'main/directories/directories_view.html'
})
export class DirDetailComponent extends AppDetailComponent {
    constructor(route: ActivatedRoute, rService: RestService, userService : UserService) {
      super(route.params, rService, userService, 'directories');
    }

    ngOnInit() {
        super.ngOnInit();
        this.loadSubViews(['threads']);
    }
}
