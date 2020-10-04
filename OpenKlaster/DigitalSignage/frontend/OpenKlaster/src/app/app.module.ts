import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {LoginFormComponent} from './user/login-form/login-form.component';
import {NavbarComponent} from './navbar/navbar.component';
import {RegisterFormComponent} from './user/register-form/register-form.component';
import {UserPanelComponent} from './user/user-panel/user-panel.component';
import {RegisterFormService} from './user/register-form.service';
import {TokenPanelComponent} from './token/token-panel/token-panel.component';
import {MeasurementPanelComponent} from './installation/measurement-panel/measurement-panel.component';
import {InstallationPanelComponent} from './installation/installation-panel/installation-panel.component';
import {TokenPanelService} from './token/token-panel.service';
import {InstallationPanelService} from './installation/installation-panel.service';
import {CookieService} from 'ngx-cookie-service';
import {LoginFormService} from './user/login-form.service';
import {SingleInstallationPanelComponent} from './installation/single-installation-panel/single-installation-panel.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSliderModule} from "@angular/material/slider";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {TokenGenerationPanelComponent} from './token/token-generation-panel/token-generation-panel.component';
import {InstallationGenerationPanelComponent} from './installation/installation-generation-panel/installation-generation-panel.component';
import {MatListModule} from "@angular/material/list";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { RequestResponseComponentComponent } from './request-response-component/request-response-component.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatButtonToggleModule} from "@angular/material/button-toggle";

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
    SingleInstallationPanelComponent,
    TokenGenerationPanelComponent,
    InstallationGenerationPanelComponent,
    RequestResponseComponentComponent
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
            {path: 'tokenGeneration', component: TokenGenerationPanelComponent},
            {path: 'installations', component: InstallationPanelComponent},
            {path: 'installations/:id', component: SingleInstallationPanelComponent},
            {path: 'installationGeneration', component: InstallationGenerationPanelComponent}
        ]),
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule,
        MatMenuModule,
        MatIconModule,
        MatButtonModule,
        MatListModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatInputModule,
        MatProgressSpinnerModule,
        MatButtonToggleModule,
    ],
  providers: [
    RegisterFormService,
    LoginFormService,
    TokenPanelService,
    InstallationPanelService,
    CookieService,
    InstallationPanelComponent,
    RequestResponseComponentComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
