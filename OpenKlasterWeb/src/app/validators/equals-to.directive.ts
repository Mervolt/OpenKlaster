import {Directive, Input} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from "@angular/forms";

@Directive({
  selector: '[appEqualsTo]',
  providers: [{provide: NG_VALIDATORS, useExisting: EqualsToDirective, multi: true}]
})
export class EqualsToDirective implements Validator {

  @Input('appEqualsTo') equalsTo: string;

  constructor() {}

  validate(control: AbstractControl): ValidationErrors | null {
    return this.equalsTo === control.value ? null : {
      validateEqualsTo: {
        valid: false
      }
    };
  }

}
