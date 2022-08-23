import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { LoginService } from '../../../app/login/login.service';
import { AccountService } from '../../../app/core/auth/account.service';
import { Account } from '../../../app/core/auth/account.model';

declare const $: any;

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;

  account: Account | null = null;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  public menuToggle() {
    let toggle = $('#header-toggle');
    let nav = $('#nav-bar');
    let bodypd = $('#body-pd');
    let headerpd = $('#header');

    nav.toggleClass('show');
    toggle.toggleClass('bx-x');
    bodypd.toggleClass('body-pd');
    headerpd.toggleClass('body-pd');

    // const showNavbar = (toggleId, navId, bodyId, headerId) => {
    //   const toggle = document.getElementById(toggleId),
    //     nav = document.getElementById(navId),
    //     bodypd = document.getElementById(bodyId),
    //     headerpd = document.getElementById(headerId);

    //   // Validate that all variables exist
    //   if (toggle && nav && bodypd && headerpd) {
    //     toggle.addEventListener('click', () => {
    //       // show navbar
    //       nav.classList.toggle('show');
    //       // change icon
    //       toggle.classList.toggle('bx-x');
    //       // add padding to body
    //       bodypd.classList.toggle('body-pd');
    //       // add padding to header
    //       headerpd.classList.toggle('body-pd');
    //     });
    //   }
    // };

    // showNavbar('header-toggle', 'nav-bar', 'body-pd', 'header');

    // /*===== LINK ACTIVE =====*/
    // const linkColor = document.querySelectorAll('.nav_link');

    // function colorLink() {
    //   if (linkColor) {
    //     linkColor.forEach(l => l.classList.remove('active'));
    //     this.classList.add('active');
    //   }
    // }
    // linkColor.forEach(l => l.addEventListener('click', colorLink));
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });

    //this.init();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }
}
