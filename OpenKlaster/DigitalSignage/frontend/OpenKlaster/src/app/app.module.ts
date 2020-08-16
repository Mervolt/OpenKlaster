import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { NavbarComponent } from './navbar/navbar.component';
import { RegisterFormComponent } from './register-form/register-form.component';
import { UserPanelComponent } from './user-panel/user-panel.component';
import { RegisterFormService } from './register-form.service';
import { TokenPanelComponent } from './token-panel/token-panel.component';
import { MeasurementPanelComponent } from './measurement-panel/measurement-panel.component';
import { InstallationPanelComponent } from './installation-panel/installation-panel.component';
import { TokenComponent } from './token/token.component';
import { TokenPanelService } from './token-panel.service';
import { InstallationPanelService } from './installation-panel.service';
import { CookieService } from 'ngx-cookie-service';
import { LoginFormService } from './login-form.service';
import { SingleInstallationPanelComponent } from './single-installation-panel/single-installation-panel.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSliderModule} from "@angular/material/slider";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    NavbarComponent,
    RegisterFormComponent,
    UserPanelComponent,
    TokenPanelComponent,
    MeasurementPanelComponent,
    InstallationPanelComponent,
    TokenComponent,
    SingleInstallationPanelComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatSliderModule,
    RouterModule.forRoot([
      {path: '', component: RegisterFormComponent},
      {path: 'login', component: LoginFormComponent},
      {path: 'user', component: UserPanelComponent},
      {path: 'token', component: TokenPanelComponent},
      {path: 'installations', component: InstallationPanelComponent},
      {path: 'installations/:id', component: SingleInstallationPanelComponent}
    ]),
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatIconModule,
  ],
  providers: [
    RegisterFormService,
    LoginFormService,
    TokenPanelService,
    InstallationPanelService,
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
