import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';

import { UserService } from '../user.service';
import { AuthService} from '../auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.css']
})
export class AuthenticationComponent implements OnInit {

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
    private translate: TranslateService) {}
  
  minLengthPass:number = environment.minLengthPass;
  loginForm: FormGroup;
  submitted: boolean = false;
  user = {
    'userName': '',
    'password': ''
  };
  
  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(this.minLengthPass)]]
    });
  }

  get form() {
    return this.loginForm.controls;
  }

  private showLastAccess(info: any) {
    let lastLoginDate = new Date(info.user.lastLoginDate).toLocaleString();
    this.showSuccess(lastLoginDate);
  }

  onSubmit() {
    this.submitted = true;

    if (!this.loginForm.invalid) {
      this.user.userName = this.loginForm.controls['username'].value;
      this.user.password = this.loginForm.controls['password'].value;

      this.userService.doLogin(this.user).subscribe(response => {

        this.authService.login(response);
        this.showLastAccess(response);
        this.userService.saveLastAccessDate().subscribe();
        this.router.navigate(['/welcome']);

      }, error => {

        if (error.error.globalError == 'usuario o contraseña erróneos') {
          this.showError('LOGIN.incorrect-user-pass');
        } else {
          this.showError('LOGIN.unk-loggin');
        }

      });
    }
  }

  showSuccess(message: string) {
    this.translate.get('LOGIN.last-access').subscribe(res => {
      this.toastr.success(message, res);
    });
  }

  showError(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.error('', res);
    });
  }
}
