export class EndpointHolder {
  static baseProdUrl = ":)"
  static baseUrl = "http://localhost:8082/"
  static baseDevUrl = EndpointHolder.baseUrl + "api/1/"
  static userEndpoint = EndpointHolder.baseDevUrl + "user";
  static tokenEndpoint = EndpointHolder.baseDevUrl + "token";
  static loginEndpoint = EndpointHolder.baseDevUrl + "user/login";
  static installationEndpoint = EndpointHolder.baseDevUrl + "installations";
  static installationsEndpoint = EndpointHolder.baseDevUrl + "installations/all";
  static swaggerEndpoint = EndpointHolder.baseUrl + "swagger.json";
}