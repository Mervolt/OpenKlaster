import {Load} from './Load';
import {Inverter} from './Inverter';
import {Source} from './Source';

export class Installation{
  constructor(public installationId: string, public installationType: string, public longitude: number,
              public latitude: number, public description: string, public load: Load, public inverter: Inverter,
              public source: Source) {
  }
}
