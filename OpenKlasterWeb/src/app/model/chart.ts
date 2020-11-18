import {SafeResourceUrl} from '@angular/platform-browser';

export class Chart {
  name: Date;
  data: SafeResourceUrl;

  constructor(name: Date, data: SafeResourceUrl) {
    this.name = name;
    this.data = data;
  }
}
