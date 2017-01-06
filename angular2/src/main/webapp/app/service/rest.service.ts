import {Injectable, EventEmitter} from '@angular/core';
import {Http, RequestOptions, Headers, Response} from '@angular/http';
import {Observable, AsyncSubject, Subject, Observer} from 'rxjs/Rx';

@Injectable()
export class RestService {
  private subject : Subject<Response>;
  public searchEvent: EventEmitter<any> = new EventEmitter<any>();
  private jsonOptions = new RequestOptions({headers : new Headers({ 'Content-Type': 'application/json' })});

  constructor(private $http: Http) {}

  observeServicesLink(service : string) : Observable<string> {
    var http : Http = this.$http;
    var self : RestService = this;
    var filter = this.filter;

    var observable : Observable<Response> = Observable.create(​function​(observer:Observer<Response>) {
​ 	    ​if​ (!self.subject) {
​ 	      self.subject = ​new​ AsyncSubject<Response>();
  ​	      http.get('rest/').subscribe(self.subject);
​ 	    }
​ ​	    ​return​ self.subject.subscribe(observer);
​ 	  });

    ​return​ observable.map<string>(_=> filter(_.json()._links[service].href));
  }

  filter(link : string) {
    return link.indexOf('{')<=0?link:link.substring(0, link.indexOf('{'));
  }

  observeService(service : string, path? : string) : Observable<Response> {
    return this.observeServicesLink(service). flatMap<Response>(
            (link:string)=> this.$http.get(link + (path?path:''))
          );
  }

  search(service:string, searchObj: any) : void {
    var http : Http = this.$http;
    this.observeServicesLink(service)
              .flatMap<Response>(
                 (link:string) => http.post(link+'/search/advanced', JSON.stringify(searchObj), this.jsonOptions))
              .subscribe(
                (_:Response) => this.searchEvent.emit(_.json()._embedded?_.json()._embedded[service]:[]),
                error => console.log(error)
              );
  }

  post(service:string, submittedObj: any) : Observable<Response> {
    var http : Http = this.$http;
    return this.observeServicesLink(service)
              .flatMap<Response>(
                 (link:string) => http.post(link, JSON.stringify(submittedObj), this.jsonOptions));
  }

}
