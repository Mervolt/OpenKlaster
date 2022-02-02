import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {LoginFormComponent} from './user/login-form/login-form.component';
import {NavbarComponent} from './navbar/navbar.component';
import {RegisterFormComponent} from './user/register-form/register-form.component';
import {UserService} from './service/user.service';
import {TokenPanelComponent} from './token/token-panel/token-panel.component';
import {InstallationPanelComponent} from './installation/installation-panel/installation-panel.component';
import {TokenService} from './service/token/token.service';
import {CookieService} from 'ngx-cookie-service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSliderModule} from '@angular/material/slider';
import {MatMenuModule} from '@angular/material/menu';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {InstallationAddPanelComponent} from './installation/installation-add-panel/installation-add-panel.component';
import {MatListModule} from '@angular/material/list';
import {MatPaginatorIntl, MatPaginatorModule} from '@angular/material/paginator';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {RequestResponseComponentComponent} from './request-response-component/request-response-component.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatSelectModule} from '@angular/material/select';
import {CredentialsFormComponent} from './credentials/credentials-form/credentials-form.component';
import {MatTabsModule} from '@angular/material/tabs';
import {InstallationDetailsBoxComponent} from './installation/installation-details-box/installation-details-box.component';
import {GoalRepresentationComponentComponent} from './installation/goal-representation-component/goal-representation-component.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {DeleteInstallationDialogComponent} from './installation/delete-installation-dialog/delete-installation-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {EditInstallationComponent} from './installation/edit-installation/edit-installation.component';
import {CredentialsTokenComponent} from './credentials/credentials-token/credentials-token.component';
import {CredentialsUserpassComponent} from './credentials/credentials-userpass/credentials-userpass.component';
import {MatTableModule} from '@angular/material/table';
import {ConfirmationDialogPopupComponent} from './components/confirmation-dialog-popup/confirmation-dialog-popup.component';
import {MatCardModule} from '@angular/material/card';
import {InstallationService} from './service/installation/installation.service';
import {HttpClientInterceptor} from './service/interceptors/http-client-interceptor.service';
import {ChartsComponent} from './charts/charts.component';
import {DynamicFormQuestionComponent} from './components/Question-boxes/dynamic-form-question/dynamic-form-question.component';
import {DynamicFormComponent} from './components/Question-boxes/dynamic-form/dynamic-form.component';
import {SubmitButtonComponent} from './components/submit-button/submit-button.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {UserEditionComponent} from './user/user-edition/user-edition.component';
import {EditionInputComponent} from './components/edition-input/edition-input.component';
import {UserPanelComponent} from './user/user-panel/user-panel.component';
import {EqualsToDirective} from './validators/equals-to.directive';
import {InstallationViewComponent} from './installation/installation-view/installation-view.component';
import {DescriptionTileComponent} from './installation/installation-view/description-tile/description-tile.component';
import {DashboardPropertiesComponent} from './components/dashboard-properties/dashboard-properties.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {MatStepperModule} from '@angular/material/stepper';
import {NgxSpinnerModule} from 'ngx-spinner';
import {ToastrModule} from 'ngx-toastr';
import { DigitalSignageComponent } from './digital-signage/digital-signage.component';
import {TranslateLoader, TranslateModule, TranslateService} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {I18Paginator} from './i18n/I18Paginator';
import {ClipboardModule} from 'ngx-clipboard';
import {MatTooltipModule} from '@angular/material/tooltip';
import { DeleteTokenDialogComponent } from './token/delete-token-dialog/delete-token-dialog.component';
import { InstallationSummaryComponent } from './installation-summary/installation-summary/installation-summary.component';
import { SelectInstallationDialogComponent } from './installation-summary/select-installation-dialog/select-installation-dialog.component';
import { SummarySlideDescriptionComponent } from './installation-summary/installation-summary/summary-slide-description/summary-slide-description.component';
import { EnvironmentPanelComponent } from './installation-summary/installation-summary/environment-panel/environment-panel.component';
import { EnvironmentChartComponent } from './installation-summary/installation-summary/environment-chart/environment-chart.component';
import {
  CategoryService,
  ChartModule,
  DataLabelService,
  LegendService,
  LineSeriesService,
  TooltipService
} from "@syncfusion/ej2-angular-charts";


@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    NavbarComponent,
    RegisterFormComponent,
    TokenPanelComponent,
    InstallationPanelComponent,
    InstallationAddPanelComponent,
    RequestResponseComponentComponent,
    CredentialsFormComponent,
    InstallationDetailsBoxComponent,
    GoalRepresentationComponentComponent,
    DeleteInstallationDialogComponent,
    EditInstallationComponent,
    CredentialsTokenComponent,
    CredentialsUserpassComponent,
    ConfirmationDialogPopupComponent,
    ChartsComponent,
    DynamicFormQuestionComponent,
    DynamicFormComponent,
    SubmitButtonComponent,
    UserEditionComponent,
    EditionInputComponent,
    UserPanelComponent,
    EqualsToDirective,
    InstallationViewComponent,
    DescriptionTileComponent,
    DashboardPropertiesComponent,
    DigitalSignageComponent,
    DeleteTokenDialogComponent,
    InstallationSummaryComponent,
    SelectInstallationDialogComponent,
    SummarySlideDescriptionComponent,
    EnvironmentPanelComponent,
    EnvironmentChartComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatSliderModule,
    RouterModule.forRoot([
      {path: 'register', component: RegisterFormComponent},
      {path: 'login', component: LoginFormComponent},
      {path: 'user', component: UserPanelComponent},
      {path: 'installations', component: InstallationPanelComponent},
      {path: 'installations/:id', component: InstallationViewComponent},
      {path: 'installationGeneration', component: InstallationAddPanelComponent},
      {path: 'editInstallation/:id', component: EditInstallationComponent},
      {path: 'charts', component: ChartsComponent},
      {path: 'digitalSignage', component: DigitalSignageComponent},
      {path: 'installationSummary/:id', component: InstallationSummaryComponent},
      {path: 'installationSummary', component: InstallationSummaryComponent}
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
    MatTableModule,
    MatCardModule,
    NgbModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatStepperModule,
    NgxSpinnerModule,
    ToastrModule.forRoot(),
    MatTooltipModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: httpTranslation,
        deps: [HttpClient]
      }
    }),
    ClipboardModule,
    ChartModule
  ],
  providers: [
    {
      provide: MatPaginatorIntl, deps: [TranslateService],
      useFactory: (translateService: TranslateService) => new I18Paginator(translateService).getPaginatorIntl()
    },
    UserService,
    TokenService,
    InstallationService,
    CookieService,
    InstallationPanelComponent,
    RequestResponseComponentComponent,
    {
      provide: MAT_DATE_LOCALE, useValue: 'pl-PL'
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpClientInterceptor,
      multi: true,
    },
    CategoryService,
    LegendService,
    TooltipService,
    DataLabelService,
    LineSeriesService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

export function httpTranslation(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
