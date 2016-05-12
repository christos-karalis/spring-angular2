import {Component} from 'angular2/core';
import {FORM_DIRECTIVES, FormBuilder,
        ControlGroup, AbstractControl,
        Validators} from 'angular2/common';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import {RouteParams} from 'angular2/router';
import {RestService} from './services';
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
  templateUrl:'main/threads/threads_view.html',
  directives: [LinkToComponent, FORM_DIRECTIVES]
})
class ThreadDetailComponent extends AppDetailComponent {
  myForm: ControlGroup;
  sku: AbstractControl;

  constructor(_routeParams: RouteParams,
              rService : RestService,
              fb: FormBuilder) {
    super(_routeParams, rService, 'threads');
    this.myForm = fb.group({
      'sku':  ['', Validators.compose([Validators.maxLength(10), Validators.required])]
    });

    this.sku = this.myForm.controls['sku'];
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

  onSubmit(value: string): void {
    console.log('you submitted value: ', value, ' ', this.myForm.controls, ' ', this.myForm.status);
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
