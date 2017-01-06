import { NgModule, OnInit }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { LocationStrategy, HashLocationStrategy} from '@angular/common';
import { routing,
         appRoutingProviders }    from './app.routing';
import { CommonModule }   from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';

import {Component} from '@angular/core';
import {HttpModule } from '@angular/http';
import {LoginComponent} from './login.component'
import {InfoBoxComponent, LinkToComponent} from './app.directive';
import {UserService, RestService} from './services';
import { DirListComponent, DirDetailComponent } from './dir.component';
import { ThreadListComponent, ThreadDetailComponent } from './thread.component';
import {HeaderSearchComponent, HeaderLoginComponent} from './header.component';

@Component({
  selector: 'main-page',
  templateUrl:  `main/common/trend.html`,
})
export class AppRouterComponent implements OnInit {
  ngOnInit(): void {}
}

@NgModule({
  imports: [
  NgbModule.forRoot(),
  CommonModule,
  BrowserModule,
  HttpModule,
  FormsModule,
  ReactiveFormsModule,
  routing,
 ],
  declarations: [
    AppRouterComponent,
    DirListComponent, DirDetailComponent,
    ThreadListComponent, ThreadDetailComponent,
    HeaderSearchComponent, HeaderLoginComponent,
    LinkToComponent, InfoBoxComponent, LoginComponent],
  providers: [ appRoutingProviders, RestService, UserService, {provide: LocationStrategy, useClass: HashLocationStrategy} ],
  bootstrap:    [ AppRouterComponent ]
})
export class AppModule { }
