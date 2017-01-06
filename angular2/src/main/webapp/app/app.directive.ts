import {Component, Query, QueryList, Input, OnInit,
        TemplateRef, ViewContainerRef, ElementRef}
        from '@angular/core';
import {RestService} from './services';

@Component({
  selector: 'link-to',
  template: `<a href="#/{{path}}/{{id}}" ng-class="linkIcon">{{label}}</a>`
  // directives: [LinkToComponent]
})
export class LinkToComponent {
  @Input() label: string;
  @Input() id: string;
  @Input() path: string;
}

@Component({
  selector: 'infobox',
  template: `
              <div class="sidebox" >
                  <h3 ng-i18next>{{title}}</h3>
                  <div class="divline"></div>
                  <div class="blocktxt">
                      <ul class="list">
                        <li *ngFor="let result of results">
                          <link-to param='test' [path]="path" [label]="result[label]" [id]="result[id]"></link-to>
                        </li>
                      </ul>
                  </div>
              </div>
            `
  // directives: [LinkToComponent]
})
        export class InfoBoxComponent implements OnInit {
  @Input()
  private path : string;
  @Input()
  private label : string;
  @Input()
  private id : string;
  @Input('param-sort')
  private paramSort : string;
  @Input('param-size')
  private paramSize : string;
  @Input()
  private title : string;
  private results : string;

  constructor(private rService : RestService) {}

  ngOnInit() {
    this.rService.observeService(this.path, "?"+(this.paramSort?'sort='+this.paramSort+"&":"")
                                           +(this.paramSize?"size="+this.paramSize+"&":""))
            .subscribe(res => this.results = res.json()._embedded[this.path],
                  error => console.log('Error loading : ' + error)
                 );
  }
}
