import {Observable} from 'rxjs/Rx';
import { Router, ActivatedRoute } from '@angular/router';
import {UserService, RestService} from './../services';
import {Component, Type} from '@angular/core';

@Component({
  selector: 'header-login',
  templateUrl: 'main/common/header_login.html'
})
export class HeaderLoginComponent {

  constructor(private userService:UserService) {}

  logout() { this.userService.logout(); }
  isAuthenticated() { return this.userService.isAuthenticated(); }
}

@Component({
  selector: 'header-search',
  templateUrl: 'main/common/header_search.html'
})
export class HeaderSearchComponent {
  private searchObject = {'operator': 'OR', 'operands' : {}}

  constructor(private route : ActivatedRoute,
              private restService:RestService,
              private router: Router) {}

  public search(searchText : string) {
    this.searchObject.operands={ title : searchText, description: searchText}
    this.router.navigate(['directories']).then(value=>{
      this.restService.search('directories', this.searchObject);
    });
  }
}
