export class EndpointHolder {
  static baseProdUrl = ":)"
  static baseUrl = "http://localhost/"
  static baseDevUrl = EndpointHolder.baseUrl + "api/1/"
  static userEndpoint = EndpointHolder.baseDevUrl + "user";
  static tokenEndpoint = EndpointHolder.baseDevUrl + "token";
  static loginEndpoint = EndpointHolder.baseDevUrl + "user/login";
  static installationEndpoint = EndpointHolder.baseDevUrl + "installations";
  static installationsEndpoint = EndpointHolder.baseDevUrl + "installations/all";
  static swaggerEndpoint = EndpointHolder.baseDevUrl + "swagger.json";
  static chartEndpoint = EndpointHolder.baseDevUrl + "chart";
}
