import {Observable} from 'rxjs/Rx';
import {Directive, OnInit} from 'angular2/core';
import {RestService} from './services';
import {RouteParams} from 'angular2/router';

export class AppComponent implements OnInit {
 [key: string]: any;

  constructor(private rService : RestService,
              private serviceName : string) {}

  ngOnInit() {
    this.rService.getService(this.serviceName)
        .then(res => {this[this.serviceName] = res.json()._embedded[this.serviceName];},
                  error => console.log('Error loading : ' + error));
  }
}

export class AppDetailComponent implements OnInit {
  protected promise : Promise<any>;
  protected data : any;

  constructor(protected _routeParams: RouteParams,
              protected rService : RestService,
              protected serviceName : string) {}

  ngOnInit() {
    this.promise = this.rService.getService(this.serviceName, '/'+this._routeParams.get('id'))
            .then(res => this.data = res.json(),
                  error => console.log('Error loading : ' + error)
                 );
  }

  loadSubViews(views : string[]) {
    let promises : Array<Promise<any>> = []
    for (let view of views) {
      promises.push(this.rService
                          .getService(this.serviceName, '/'+this._routeParams.get('id')+'/'+view)
                          .then(_=>_.json()._embedded[view]))
    }
    return promises;
  }
}
