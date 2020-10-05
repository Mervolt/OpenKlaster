import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {LoginFormComponent} from './user/login-form/login-form.component';
import {NavbarComponent} from './navbar/navbar.component';
import {RegisterFormComponent} from './user/register-form/register-form.component';
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
import {InstallationGenerationPanelComponent} from './installation/installation-generation-panel/installation-generation-panel.component';
import {MatListModule} from "@angular/material/list";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {RequestResponseComponentComponent} from './request-response-component/request-response-component.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatSelectModule} from "@angular/material/select";
import {CredentialsFormComponent} from './credentials/credentials-form/credentials-form.component';
import {MatTabsModule} from "@angular/material/tabs";
import {InstallationDetailsBoxComponent} from './installation/installation-details-box/installation-details-box.component';
import {GoalRepresentationComponentComponent} from './installation/goal-representation-component/goal-representation-component.component';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {DeleteInstallationDialogComponent} from './installation/delete-installation-dialog/delete-installation-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {EditInstallationComponent} from './installation/edit-installation/edit-installation.component';
import {CredentialsTokenComponent} from './credentials/credentials-token/credentials-token.component';
import {CredentialsUserpassComponent} from './credentials/credentials-userpass/credentials-userpass.component';
import {InstallationListItemComponent} from './installation/installation-list-item/installation-list-item.component';
import {MatTableModule} from "@angular/material/table";

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    NavbarComponent,
    RegisterFormComponent,
    TokenPanelComponent,
    MeasurementPanelComponent,
    InstallationPanelComponent,
    SingleInstallationPanelComponent,
    InstallationGenerationPanelComponent,
    RequestResponseComponentComponent,
    CredentialsFormComponent,
    InstallationDetailsBoxComponent,
    GoalRepresentationComponentComponent,
    DeleteInstallationDialogComponent,
    EditInstallationComponent,
    CredentialsTokenComponent,
    CredentialsUserpassComponent,
    InstallationListItemComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatSliderModule,
    RouterModule.forRoot([
      {path: '', component: RegisterFormComponent},
      {path: 'login', component: LoginFormComponent},
      {path: 'token', component: TokenPanelComponent},
      {path: 'installations', component: InstallationPanelComponent},
      {path: 'installations/:id', component: SingleInstallationPanelComponent},
      {path: 'installationGeneration', component: InstallationGenerationPanelComponent},
      {path: 'editInstallation/:id', component: EditInstallationComponent}
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
    MatSelectModule,
    MatTabsModule,
    MatProgressBarModule,
    MatCheckboxModule,
    MatDialogModule,
    MatTableModule
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
