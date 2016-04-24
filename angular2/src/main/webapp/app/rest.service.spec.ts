/// <reference path="../../../../typings/browser/ambient/jasmine/index.d.ts" />

import {it, describe, expect, setBaseTestProviders,
    beforeEachProviders, beforeEach, inject, injectAsync,
    TestComponentBuilder} from 'angular2/testing';
import {BaseRequestOptions, Response, ResponseOptions, Http} from 'angular2/http';
import {MockBackend, MockConnection} from 'angular2/http/testing';
import {provide} from "angular2/core";
import {RestService} from './rest.service';

describe('Threads page test', () => {
    var mockService: any;
    var response: any = `{ "_links" : { "self" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread{?page,size,sort}", "templated" : true }, "search" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/search" } }, "_embedded" : { "threads" : [ { "id" : 1, "title" : "Spring MVC form Validation: How to remove these lines from getting displayed", "countOfPosts" : 1, "_links" : { "self" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/1" }, "posts" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/1/posts" }, "directory" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/1/directory" } } }, { "id" : 2, "title" : "Spring Data JPA aggregation group by period of time", "countOfPosts" : 1, "_links" : { "self" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/2" }, "posts" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/2/posts" }, "directory" : { "href" : "http://localhost:8080/forum-spring-angular/rest/thread/2/directory" } } } ] }, "page" : { "size" : 20, "totalElements" : 5, "totalPages" : 1, "number" : 0 } }`;

    beforeEachProviders(() => [
      RestService,
      BaseRequestOptions,
      MockBackend,
      provide(Http, {
        useFactory: (backend: MockBackend, defaultOptions: BaseRequestOptions) => {
          return new Http(backend, defaultOptions);
        },
        deps: [MockBackend, BaseRequestOptions]
      })
    ]);

    beforeEach(inject([MockBackend], (backend: MockBackend) => {
      const baseResponse = new Response(new ResponseOptions({body: `{ "_links" : { "directories" : { "href" : "/rest/directory", "templated" : true}, "users" : { "href" : "/rest/user", "templated" : true}, "threads" : { "href" : "/rest/thread", "templated" : true}, "posts" : { "href" : "/test/posts.json", "templated" : true} } }`}));
      const threadsResponse = new Response(new ResponseOptions({body: response}));
      const emptyResponse = new Response(new ResponseOptions({body: ''}));
      backend.connections.subscribe((c: MockConnection) => {console.log(c.request.url);c.mockRespond(c.request.url=='rest/'?baseResponse:c.request.url=='/rest/thread'?threadsResponse:emptyResponse);});
    }));

    it('should return response when subscribed to getUsers',
      inject([RestService], (restService: RestService) => {
        restService.getService('threads').then((res) => {
          expect(res.json()._links.self.href).toBe('http://localhost:8080/forum-spring-angular/rest/thread{?page,size,sort}');
        }, (error) => console.log(error) );
      })
    );

});
