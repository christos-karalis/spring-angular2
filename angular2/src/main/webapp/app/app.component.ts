import {OnInit} from '@angular/core';
import {Params} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';

import {RestService, UserService} from './services';

export class AppComponent implements OnInit {
 [key: string]: any;

  constructor(private rService : RestService,
              protected userService : UserService,
              private serviceName : string) {}

  ngOnInit() {
    this.rService.observeService(this.serviceName)
        .subscribe(res => {this[this.serviceName] = res.json()._embedded[this.serviceName];},
                  error => console.log('Error loading : ' + error));
  }
}

export class AppDetailComponent implements OnInit {
  protected data : { [key:string]:any } = {};

  constructor(protected _routeParams: Observable<Params>,
              protected rService : RestService,
              protected userService : UserService,
              protected serviceName : string) {}

  ngOnInit() {
    this._routeParams.switchMap((params: Params) => this.rService.observeService(this.serviceName, '/'+params['id']))
            .subscribe((res:Response) => { for (let key in res.json()) { this.data[key] = res.json()[key] } },
                  error => console.log('Error loading : ' + error)
                 );
  }

  loadSubViews(views : string[]) {
    var self : AppDetailComponent = this;
    for (let view of views) {
      this._routeParams.switchMap((params: Params) => self.rService
                          .observeService(self.serviceName, '/'+params['id']+'/'+view))
                          .subscribe((_:Response)=> self.data[view] = _.json()._embedded[view]);
    }
  }
}
