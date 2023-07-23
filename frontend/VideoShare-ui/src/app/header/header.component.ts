import {Component, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {Router} from "@angular/router";
import {CallbackComponent} from "../callback/callback.component";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{

  isAuthenticated:boolean=false;

  constructor(private oidcSecurityService:OidcSecurityService,private router :Router) {
  }

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(({isAuthenticated})=>{
      this.isAuthenticated=isAuthenticated;
    })

  }

  login() {
    this.oidcSecurityService.authorize();
    this.router.navigateByUrl('');
  }
  logoff(){
    this.oidcSecurityService.logoffLocal();
    this.router.navigateByUrl('');
  }
}
