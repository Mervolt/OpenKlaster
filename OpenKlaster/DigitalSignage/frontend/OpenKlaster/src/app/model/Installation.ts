import {Load} from './Load';
import {Inverter} from './Inverter';
import {Source} from './Source';

export class Installation{
  //TODO maybe some static fromDto would be also a good idea? It could take HTTP API responses as parameters
  // Of course affects every other model class
  constructor(public installationId: string, public installationType: string, public longitude: number,
              public latitude: number, public description: string, public load: Load, public inverter: Inverter,
              public source: Source) {
  }
}
