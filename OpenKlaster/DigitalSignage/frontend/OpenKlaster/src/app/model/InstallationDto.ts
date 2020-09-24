import {Installation} from "./Installation";

export class InstallationDto {

static installationIdKey: string = '_id';
static installationTypeKey: string = 'installationType';
static longitudeKey: string = 'longitude';
static latitudeKey: string = 'latitude';
static descriptionKey: string = 'description';
static loadKey: string = 'load';
static inverterKey: string = 'inverter';
static sourceKey: string = 'source'

  static fromDto(response: Object): Installation {
    let installation = new Installation()
    installation.installationId = response[this.installationIdKey]
    installation.installationType = response[this.installationTypeKey]
    installation.longitude = response[this.longitudeKey]
    installation.latitude = response[this.latitudeKey]
    installation.description = response[this.descriptionKey]
    installation.load = response[this.loadKey]
    installation.inverter = response[this.inverterKey]
    installation.source = response[this.sourceKey]
    return installation
  }
}
