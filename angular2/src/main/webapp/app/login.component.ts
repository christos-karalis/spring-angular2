import {Component} from 'angular2/core';
import {FORM_DIRECTIVES} from 'angular2/common';
import {UserService} from './services';

@Component({
  selector: 'login',
  template: `
      <fieldset>
          <div class="form-group">
              <label for="login:username" class="col-lg-4 control-label">Username</label>
              <div class="col-lg-8">
                  <input type="text" class="form-control" id="login:username" placeholder="Username" [(ngModel)]="model.username">
              </div>
          </div>
          <div class="form-group">
              <label for="login:password" class="col-lg-4 control-label">Password</label>
              <div class="col-lg-8">
                  <input type="password" class="form-control" id="login:password" placeholder="Password" [(ngModel)]="model.password">
              </div>
          </div>

          <div class="form-group">
              <div class="col-lg-10 col-lg-offset-2">
                  <button type="submit" class="login-button" (click)='login()' >Login</button>
              </div>
          </div>
      </fieldset>
      `,
  directives: [FORM_DIRECTIVES]
})
export class LoginComponent {
  private model:{username:string, password:string} = {username:'user', password:'password'};

  constructor(private userService: UserService) {}

  login() {
    this.userService.login(this.model.username, this.model.password);
  }

  logout() {
    this.userService.logout();
  }
}
