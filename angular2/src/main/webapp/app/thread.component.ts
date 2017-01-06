import {Component} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {FormBuilder,
        FormGroup, FormControl,
        Validators} from '@angular/forms';
import {Response} from '@angular/http';
// import {RouteConfig, ROUTER_DIRECTIVES, RouteParams} from '@angular/router';
import {RestService, UserService} from './services';
import {AppComponent, AppDetailComponent} from './app.component';
import {LinkToComponent} from './app.directive';
import {Observable} from 'rxjs/Rx';

@Component({
  templateUrl:`main/threads/threads.html`
})
export class ThreadListComponent extends AppComponent {
  constructor(rService : RestService, userService : UserService) {
    super(rService, userService, 'threads'); }
}

@Component({
  templateUrl:'main/threads/threads_view.html'
})
export class ThreadDetailComponent extends AppDetailComponent {
  postForm: FormGroup;

  constructor(protected route : ActivatedRoute,
              protected rService : RestService,
              userService : UserService) {
    super(route.params, rService, userService, 'threads');
    let group: any = {};
    group['message'] = new FormControl('', Validators.required);

    this.postForm = new FormGroup(group);
  }

  ngOnInit() {
    super.ngOnInit();
    this.loadSubViews(['posts']);
  }

  onSubmit(value: any): void {
    var self : ThreadDetailComponent = this;
    var id : string;
    Observable.combineLatest<string,string>(
                    this._routeParams.switchMap((params: Params) => (id = params['id'])),
                    this.rService.observeServicesLink('threads')
              ).flatMap<Response>(([id,link], index) => this.rService.post('posts', {thread:link+'/'+id, body:value['message']})
              ).flatMap(response=>this.rService.observeService('threads', '/'+id+'/posts')
            ).take(1).subscribe(response=>{self.data['posts']=response.json()._embedded['posts'];self.postForm.reset();}
                              ,error=>console.log(error));
  }
}
