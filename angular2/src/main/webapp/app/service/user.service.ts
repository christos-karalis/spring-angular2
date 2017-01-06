import {Injectable} from '@angular/core';
import {Http} from '@angular/http';

@Injectable()
export class UserService {

  private authenticated:boolean = false;

  constructor(private $http: Http) {
    this.$http.get('rest/user/search/findCurrentUser')
              .subscribe(response => this.authenticated=(response.json().username!=='anonymous'));
  }

  isAuthenticated() {
    return this.authenticated;
  }

  login(username:string, password:string) {
    this.$http.post('authenticate?password='+password+'&username='+username, null)
            .subscribe(response => this.authenticated=true);
  }

  logout() {
    this.$http.post('logout', null).subscribe(response=>this.authenticated=false);
  }

}
