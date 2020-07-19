import { Component, OnInit } from '@angular/core';
import { RegisterFormService } from '../register-form.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  registerForm;

  constructor(public service: RegisterFormService, private formBuilder: FormBuilder) {
    this.registerForm = this.formBuilder.group({
      username: '',
      password: ''
    });
  }

  onSubmit(event){
    console.log(event);
  }

  ngOnInit(): void {
  }

}
