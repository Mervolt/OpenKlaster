export class EndpointHolder {
  static baseProdUrl = ":)"
  static baseDevUrl = "http://localhost:8082/api/1/"
  static userEndpoint = EndpointHolder.baseDevUrl + "user";
  static tokenEndpoint = EndpointHolder.baseDevUrl + "token";
  static loginEndpoint = EndpointHolder.baseDevUrl + "login";
  static installationEndpoint = EndpointHolder.baseDevUrl + "installations";
  static installationsEndpoint = EndpointHolder.baseDevUrl + "installations/all";
}
