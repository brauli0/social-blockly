import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';

import { UserService } from '../user.service';
import { AuthService} from '../auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
    private translate: TranslateService) {}

  minLengthPass:number = environment.minLengthPass;
  registerForm: FormGroup;
  submitted: boolean = false;
  user = {
    'userName': '',
    'password': '',
    'firstName': '',
    'lastName': '',
    'email': '',
    'role': ''
  };
  roles: string [] = ['Student', 'Teacher'];
  defaultRole: string = this.roles[0];

  ngOnInit() {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(this.minLengthPass)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      roleInput: [this.defaultRole]
    });
  }

  get form() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    
    if (!this.registerForm.invalid) {
      this.user.userName = this.registerForm.controls['username'].value;
      this.user.password = this.registerForm.controls['password'].value;
      this.user.firstName = this.registerForm.controls['firstName'].value;
      this.user.lastName = this.registerForm.controls['lastName'].value;
      this.user.email = this.registerForm.controls['email'].value;
      switch (this.registerForm.controls['roleInput'].value) {
        case "Student": this.user.role = "STUDENT";
          break;
        case "Teacher": this.user.role = "TEACHER";
          break;
      }

      this.userService.doSignUp(this.user).subscribe(response => {
        
        this.authService.login(response);
        this.showSuccess('REGISTER.signed-in!');
        this.router.navigate(['/welcome']);

      }, error => {
        // error.error.globalError pode ser undefined
        let auxError = error.error.globalError.substring(0, 17);
        if (auxError === 'ya existe usuario') {
          this.showError('REGISTER.username-exists');
        } else {
          this.showError('REGISTER.unk-error');
        }
      });
    }
  }

  showSuccess(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.success('', res);
    });
  }

  showError(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.error('', res);
    });
  }
}