import 'rxjs/Rx';
import {Injectable} from 'angular2/core';
import {Http} from 'angular2/http';
import {PromiseWrapper}  from 'angular2/src/facade/promise';

@Injectable()
export class RestService {
  links : CachedResponseWrapper<any>;

  constructor(private $http: Http) {
   this.links=new CachedResponseWrapper<string>('rest/');
    // this.links = new CachedResponseWrapper<string>('test/rest.json');
  }

  getServicesLink(service : string) {
    if (this.links.response == null) {
      this.links.response = this.$http.get(this.links.url).toPromise();
    }
    return this.links.response.then(_=> {console.log(_.json()._links[service].href);return _.json()._links[service].href;});
  }

  filter(link : string) {
    return link.indexOf('{')<=0?link:link.substring(0, link.indexOf('{'));
  }

  getService(service : string, path? : string) {
    return this.getServicesLink(service)
          .then(link => this.$http.get(this.filter(link) + (path?path:''))
                            .toPromise());
  }
}

class CachedResponseWrapper<T> {
  response : Promise<T>;
  url : string;

  constructor(url? : string) {
    this.url = url;
  }
}
