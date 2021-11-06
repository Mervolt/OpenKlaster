import {AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {QuestionControlService} from '../question-control-service';
import {QuestionBase} from '../question-base';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html',
  providers: [ QuestionControlService ]
})
export class DynamicFormComponent implements OnChanges {

  @Input() questions: QuestionBase<string>[] = [];
  @Output() credentialsEventEmitter = new EventEmitter<JSON>();
  form: FormGroup;

  constructor(private qcs: QuestionControlService) {
  }

  ngOnChanges() {
    this.form = this.qcs.toFormGroup(this.questions);
    this.form.valueChanges.subscribe(credentials => {
      this.credentialsEventEmitter.emit(credentials);
    });
  }
}
