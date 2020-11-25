import {environment} from '../../environments/environment';

export class EndpointHolder {
  static apiUrl = environment.apiUrl + "api/1/"
  static userEndpoint = EndpointHolder.apiUrl + "user";
  static tokenEndpoint = EndpointHolder.apiUrl + "token";
  static allTokensEndpoint = EndpointHolder.apiUrl + "token/all";
  static loginEndpoint = EndpointHolder.apiUrl + "user/login";
  static installationEndpoint = EndpointHolder.apiUrl + "installations";
  static installationsEndpoint = EndpointHolder.apiUrl + "installations/all";
  static credentialsEndpoint = EndpointHolder.apiUrl + 'manufacturerCredentials'
  static swaggerEndpoint = EndpointHolder.apiUrl + "swagger.json";
  static chartEndpoint = EndpointHolder.apiUrl + "chart";
  static summaryEndpoint = EndpointHolder.apiUrl + "summary"
  static selectableDatesEndpoint = EndpointHolder.apiUrl + "selectableDates";
  static digitalSignageEndpoint = environment.apiUrl + "digital-signage/index.html"
}
