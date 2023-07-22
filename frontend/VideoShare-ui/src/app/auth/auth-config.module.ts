import { NgModule } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';


@NgModule({
    imports: [AuthModule.forRoot({
        config: {
            authority: 'https://cannon.eu.auth0.com',
            postLogoutRedirectUri:"http://localhost:4200",
            redirectUrl: window.location.origin+"/callback",
            clientId: 'rmlSaClchYf8axPDxoDDikH0ZkNY8HkF',
            scope: 'openid profile offline_access email',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            secureRoutes:['http://localhost:8080'],
            customParamsAuthRequest:{
              audience: 'http://localhost:8080'
          }
        }
      })],
    exports: [AuthModule],
})
export class AuthConfigModule {}
