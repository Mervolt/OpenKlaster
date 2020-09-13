import {Load} from './Load';
import {Inverter} from './Inverter';
import {Source} from './Source';

export class Installation{
  //TODO maybe some static fromDto would be also a good idea? It could take HTTP API responses as parameters
  // Of course affects every other model class
  //MM-ANSWER InstallationDto.ts
  installationId: string
  installationType: string
  longitude: number
  latitude: number
  description: string
  load: Load
  inverter: Inverter
  source: Source
  constructor() {
    this.load = new Load()
    this.inverter = new Inverter()
    this.source = new Source()
  }


}
