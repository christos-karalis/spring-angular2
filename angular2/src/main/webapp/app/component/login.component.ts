import {Component} from '@angular/core';
import {UserService} from './../services';

@Component({
  selector: 'login',
  template: `
      <form (ngSubmit)="login()" #userForm="ngForm">
          <fieldset>
              <div class="form-group row">
                  <label for="login:username" class="col-lg-4 control-label">Username</label>
                  <div class="col-lg-8">
                      <input type="text" class="form-control" id="login:username" placeholder="Username" [(ngModel)]="user.username" name="username">
                  </div>
              </div>
              <div class="form-group row">
                  <label for="login:password" class="col-lg-4 control-label">Password</label>
                  <div class="col-lg-8">
                      <input type="password" class="form-control" id="login:password" placeholder="Password" [(ngModel)]="user.password" name="password">
                  </div>
              </div>

              <div class="form-group">
                  <div class="col-lg-10 col-lg-offset-2">
                      <button type="submit" class="login-button">Login</button>
                  </div>
              </div>
          </fieldset>
      </form>
      `
})
export class LoginComponent {
  private user:{username:string, password:string} = {username:'user', password:'password'};

  constructor(private userService: UserService) {}

  login() {
    this.userService.login(this.user.username, this.user.password);
  }

  logout() {
    this.userService.logout();
  }
}
