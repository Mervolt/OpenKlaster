import {QuestionBase} from "./question-base";

export class QuestionTextbox extends QuestionBase <string> {
  controlType = 'textbox'
  constructor(keyValue: string, labelValue: string) {
    super({
      key: keyValue,
      label: labelValue,
      required: true,
      controlType: 'textbox'
    });
  }
}
