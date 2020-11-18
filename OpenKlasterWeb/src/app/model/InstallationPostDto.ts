import {Load} from './Load';
import {Inverter} from './Inverter';
import {Source} from './Source';
import {Installation} from "./Installation";

export class InstallationPostDto {
  installationId: string
  installationType: string
  longitude: number
  latitude: number
  description: string
  load: Load
  inverter: Inverter
  source: Source
  username: string

  constructor() {
    this.load = new Load()
    this.inverter = new Inverter()
    this.source = new Source()
  }

  static fromInstallationWithUser(installation: Installation, username: string): InstallationPostDto {
    let outp = new InstallationPostDto()
    outp.installationId = installation.installationId;
    outp.installationType = installation.installationType;
    outp.longitude = installation.longitude;
    outp.latitude = installation.latitude;
    outp.description = installation.description;
    outp.load = installation.load;
    outp.inverter = installation.inverter;
    outp.source = installation.source;
    outp.username = username
    return outp
  }

}
