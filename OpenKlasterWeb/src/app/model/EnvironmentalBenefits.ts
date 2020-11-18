export class EnvironmentalBenefits {

  static treesSavedKey = "treesSaved"
  static co2ReducedKey = "co2Reduced"
  treesSaved: number
  co2Reduced: number
  constructor() {}

  static fromDto(dtoObject: Object): EnvironmentalBenefits {
    let output = new EnvironmentalBenefits();
    output.treesSaved = dtoObject[this.treesSavedKey];
    output.co2Reduced = dtoObject[this.co2ReducedKey];
    return output;
  }
}
