import 'rxjs/Rx';
import {Injectable, EventEmitter} from 'angular2/core';
import {Http, RequestOptions, Headers, Response} from 'angular2/http';
import {PromiseWrapper}  from 'angular2/src/facade/promise';

@Injectable()
export class RestService {
  private links : CachedResponseWrapper<any>;
  public searchEvent: EventEmitter<any> = new EventEmitter<any>();
  private jsonOptions = new RequestOptions({headers : new Headers({ 'Content-Type': 'application/json' })});

  constructor(private $http: Http) {
   this.links=new CachedResponseWrapper<string>('rest/');
    // this.links = new CachedResponseWrapper<string>('test/rest.json');
  }

  getServicesLink(service : string) {
    if (this.links.response == null) {
      this.links.response = this.$http.get(this.links.url).toPromise();
    }
    return this.links.response.then(_=> _.json()._links[service].href);
  }

  filter(link : string) {
    return link.indexOf('{')<=0?link:link.substring(0, link.indexOf('{'));
  }

  getService(service : string, path? : string) : Promise<Response> {
    return this.getServicesLink(service)
               .then(link => this.$http.get(this.filter(link) + (path?path:''))
                            .toPromise());
  }

  search(service:string, searchObj: any) {
    this.getServicesLink(service)
               .then(link => this.$http.post(this.filter(link)+'/search/advanced', JSON.stringify(searchObj), this.jsonOptions)
                                  .toPromise().then(_ => this.searchEvent.emit(_.json()._embedded?_.json()._embedded[service]:[]), error => console.log('Error  :' + error))
                            ,
                     error => console.log('Error :' + error));
  }


}

class CachedResponseWrapper<T> {
  response : Promise<T>;
  url : string;

  constructor(url? : string) {
    this.url = url;
  }
}
