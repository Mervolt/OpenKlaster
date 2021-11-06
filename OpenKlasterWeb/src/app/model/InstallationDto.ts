import {Installation} from './Installation';

export class InstallationDto {

  static installationIdKey = 'installationId';
  static installationTypeKey = 'installationType';
  static longitudeKey = 'longitude';
  static latitudeKey = 'latitude';
  static descriptionKey = 'description';
  static loadKey = 'load';
  static inverterKey = 'inverter';
  static sourceKey = 'source';

  static fromDto(response: Object): Installation {
    const installation = new Installation();
    installation.installationId = response[this.installationIdKey];
    installation.installationType = response[this.installationTypeKey];
    installation.longitude = response[this.longitudeKey];
    installation.latitude = response[this.latitudeKey];
    installation.description = response[this.descriptionKey];
    installation.load = response[this.loadKey];
    installation.inverter = response[this.inverterKey];
    installation.source = response[this.sourceKey];
    return installation;
  }
}
