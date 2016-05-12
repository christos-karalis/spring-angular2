import {
  Component, Directive, Input, HostListener, DynamicComponentLoader,
  ComponentRef, Provider, ReflectiveInjector, ViewContainerRef, Type,
  ViewChild, Injectable, ElementRef, AfterViewInit, ChangeDetectorRef, Inject,
  Injector
} from 'angular2/core';
import {NgClass, NgStyle} from 'angular2/common';
import {positionService} from 'ng2-bootstrap/components/position';
import {Http} from 'angular2/http';
import { DOM } from 'angular2/src/platform/dom/dom_adapter';

@Injectable()
export class PopoverOptions {
  public placement:string;
  public popupClass:string;
  public animation:boolean;
  public isOpen:boolean;
  public comp:Type;

  public constructor(options:Object) {
    Object.assign(this, options);
  }
}

function compileToComponent(template:string, directives?:Array<Type>) {
  @Component({
    template: '<'+template+'></'+template+'>', directives
  })
  class CompiledComponent {
    constructor() {}
  };
  return CompiledComponent;
}

@Component({
  selector: 'popover-container',
  directives: [NgClass, NgStyle],
  // changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="popover" role="tooltip"
      [ngStyle]="{top: top, left: left, display: display, width:width, height:height}"
      [ngClass]="classMap">
      <div class="arrow" [ngStyle]="{top: arrowTop}"></div>
      <h3 class="popover-title">{{title}}</h3>
      <div class="popover-content"></div>
    </div>`
})
export class PopoverContainerComponent implements AfterViewInit {
  /* tslint:disable */
  private classMap:any;
  private top:string = '-1000px';
  private left:string = '-1000px';
  private display:string = 'block';
  private content:string;
  private comp:Type;
  private title:string;
  private arrowTop:string = "25%";
  private placement:string;
  private popupClass:string;
  private animation:boolean;
  private isOpen:boolean;
  private appendToBody:boolean;
  private hostEl:ElementRef;
  /* tslint:enable */

  public constructor(private element:ElementRef,
                     @Inject(PopoverOptions) options:PopoverOptions,
                     private loader:DynamicComponentLoader,
                     private viewContainerRef: ViewContainerRef,
                     private injector:Injector) {
    Object.assign(this, options);
    this.classMap = {'in': false, 'fade': false};
    this.classMap[options.placement] = true;
  }

  public ngAfterViewInit():void {
    this.loader.loadAsRoot(
      compileToComponent('login', [this.comp]),
      '.popover-content',
      this.injector
    ).then((componentRef:ComponentRef) => {
      let p = positionService
        .positionElements(
          this.hostEl.nativeElement,
          this.element.nativeElement.children[0],
          this.placement, this.appendToBody);
      this.top = (p.top<0?0:p.top) + 'px';
      this.left = p.left + 'px';
      this.classMap.in = true;
      if (this.animation) {
        this.classMap.fade = true;
      }
      // this.viewContainerRef.element.nativeElement.ownerDocument.body.appendChild(this.viewContainerRef.element.nativeElement);
      // console.log(this.viewContainerRef.element.nativeElement.ownerDocument.createElement('login'));
      // let delta = this.getViewportAdjustedDelta(this.placement, positionService.offset(this.element.nativeElement),  this.element.nativeElement.children[0].offsetWidth, this.element.nativeElement.children[0].offsetHeight);
      componentRef.changeDetectorRef.detectChanges();

      // this.arrowTop = 50 * (1 - (delta.top * 2 - this.element.nativeElement.children[0].offsetHeight + 50)/50 ) + '%';
      this.arrowTop = (this.element.nativeElement.children[0].offsetHeight + 50)/50 + '%';
      console.log(this.arrowTop);
      return componentRef;
    });

  }
}

@Directive({selector: '[popover]'})
export class PopoverDirective {
  /* tslint:disable */
  @Input('popover') public content:string;
  @Input('popoverTitle') public title:string;
  @Input('popoverPlacement') public placement:string = 'top';
  @Input('popoverIsOpen') public isOpen:boolean;
  @Input('popoverComp') public comp:Type;
  @Input('popoverEnable') public enable:boolean;
  @Input('popoverAnimation') public animation:boolean = true;
  @Input('popoverAppendToBody') public appendToBody:boolean;
  /* tslint:enable */

  private visible:boolean = false;
  private popover:Promise<ComponentRef>;

  public constructor(private element:ElementRef,
                     public viewContainerRef:ViewContainerRef,
                     private $http: Http,
                     public loader:DynamicComponentLoader) {}

  @HostListener('click', ['$event', '$target'])
  public toggle():void {
    if (this.visible) {
      this.visible = false;
      this.popover.then((componentRef:ComponentRef) => {
        componentRef.destroy();
        return componentRef;
      });
      // return;
    } else {
      this.visible = true;
        let options = new PopoverOptions({
          title: this.title,
          comp: this.comp,
          placement: this.placement,
          animation: this.animation,
          hostEl: this.viewContainerRef.element
        });

        let binding = ReflectiveInjector.resolve([
          new Provider(PopoverOptions, {useValue: options})
        ]);

        this.popover = this.loader
          .loadNextToLocation(PopoverContainerComponent, this.viewContainerRef, binding)
          .then((componentRef:ComponentRef) => {
            console.log("--this.viewContainerRef.element.nativeElement.children[0].offsetHeight:" + this.viewContainerRef.element.nativeElement.offsetHeight);
            return componentRef;
          });
    }
  }
}
