import {Load} from './Load';
import {Inverter} from './Inverter';
import {Source} from './Source';

export class Installation {
  installationId: string;
  installationType: string;
  longitude: number;
  latitude: number;
  description: string;
  load: Load;
  inverter: Inverter;
  source: Source;

  constructor() {
    this.load = new Load();
    this.inverter = new Inverter();
    this.source = new Source();
  }


}
