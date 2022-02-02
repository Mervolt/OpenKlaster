import {EnvironmentalBenefits} from './EnvironmentalBenefits';

export class InstallationSummary {

  static totalEnergyKey = 'totalEnergy';
  static todayEnergyKey = 'energyProducedToday';
  static currentPowerKey = 'currentPower';
  static environmentalBenefitsKey = 'environmentalBenefits';
  totalEnergy: number;
  todayEnergy: number;
  currentPower: number;
  environmentalBenefits: EnvironmentalBenefits;
  power: Map<Date, number>;

  constructor() {
    this.environmentalBenefits = new EnvironmentalBenefits();
  }

  static fromDto(dtoObject: Object): InstallationSummary {
    const output = new InstallationSummary();
    output.totalEnergy = dtoObject[this.totalEnergyKey];
    output.todayEnergy = dtoObject[this.todayEnergyKey];
    output.currentPower = dtoObject[this.currentPowerKey];
    output.environmentalBenefits = EnvironmentalBenefits.fromDto(dtoObject[this.environmentalBenefitsKey]);
    return output;
  }

  static toChartData(powerMap: Map<Date, number>) {
    const arr = new Array<PowerChartData>();
    for (const key of Object.keys(powerMap)) {
      const date = new Date(Date.parse(key));
      const value = powerMap[key];
      arr.push({x: date, y: value});
    }
    arr.sort( (date1, date2) => (date1.x < date2.x) ? -1 : 1)
    return arr;
  }
}

export interface PowerChartData {
  x: Date,
  y: number,
}
