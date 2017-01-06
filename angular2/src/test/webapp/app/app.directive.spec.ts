/// <reference path="../../../../typings/browser/ambient/jasmine/index.d.ts" />

import {it, describe, expect, setBaseTestProviders,
    beforeEachProviders, beforeEach, injectAsync,
    TestComponentBuilder} from 'angular2/testing';
import {TEST_BROWSER_PLATFORM_PROVIDERS, TEST_BROWSER_APPLICATION_PROVIDERS} from "angular2/platform/testing/browser";
import {Directive, provide} from "angular2/core";
import {ThreadListComponent} from "./app";
import {RestService} from './app';
import {RouteParams} from 'angular2/router';

describe('Threads page test', () => {
    setBaseTestProviders(TEST_BROWSER_PLATFORM_PROVIDERS, TEST_BROWSER_APPLICATION_PROVIDERS);
    let component: ThreadListComponent;
    var mockService: any;
    var response: any;

    beforeEach((done: Function) => {
        response = jasmine.createSpyObj("response", ["json"]);
        mockService = jasmine.createSpyObj("mockService", ["getService"]);

        var json = { "_embedded": { "threads": [{ "id": 1, "countOfPosts": 1, "title": "Spring MVC form Validation: How to remove these lines from getting displayed", "_links": { "self": { "href": "http://localhost:8080/forum-spring-angular/rest/thread/1" } } }, { "id": 2, "countOfPosts": 1, "title": "Spring Data JPA aggregation group by period of time", "_links": { "self": { "href": "http://localhost:8080/forum-spring-angular/rest/thread/2" } } }, { "id": 3, "countOfPosts": 1, "title": "How to find all revisions for an entity using spring data envers?", "_links": { "self": { "href": "http://localhost:8080/forum-spring-angular/rest/thread/3" } } }] }, "_links": { "self": { "href": "http://localhost:8080/forum-spring-angular/rest/thread" } }, "page": { "size": 20, "totalElements": 5, "totalPages": 1, "number": 0 } };
        response.json.and.returnValue(json);
        mockService.getService.and.callFake(function(param: string) {
            return new Promise(function(resolve, reject) { if (true) { resolve(response); } });
        });
        done();
    });

    beforeEachProviders(() => [
        provide(RouteParams, { useValue: mockService }),
        provide(RestService, { useValue: mockService })
    ]);

    it('should be able to test', injectAsync([TestComponentBuilder], (tcb: TestComponentBuilder) => {
        return tcb.createAsync(ThreadListComponent).then((fixture) => {
            fixture.detectChanges();
            expect(mockService.getService).toHaveBeenCalled();
            return fixture;
        }).then(fixture => {
            expect(response.json).toHaveBeenCalled();
            fixture.detectChanges();

            var results = fixture.debugElement.nativeElement.querySelectorAll("div.wrap-ut");
            expect(results.length).toBe(3);
            expect(results[0].querySelector('link-to a').innerHTML).toBe('Spring MVC form Validation: How to remove these lines from getting displayed');
        });
    }));

    it('Should capitalize all words in a string', () => {
        var result = 'Golden Retriever';
        expect(result).toEqual('Golden Retriever');
    });
});
