import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';

import { UserService } from '../user.service';
import { AuthService } from '../auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService,
    private translate: TranslateService) { }

  minLengthPass:number = environment.minLengthPass;
  passwordsForm: FormGroup;
  submitted: boolean = false;
  passwords = {
    'oldPassword': '',
    'newPassword': ''
  };

  ngOnInit() {
    this.passwordsForm = this.fb.group({
      oldpassword: ['', [Validators.required, Validators.minLength(this.minLengthPass)]],
      newpassword: ['', [Validators.required, Validators.minLength(this.minLengthPass)]],
      newpasswordconf: ['', [Validators.required, Validators.minLength(this.minLengthPass)]],
    });
  }

  get form() {
    return this.passwordsForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (!this.passwordsForm.invalid) {
      
      // DEBUG
      if (!this.authService.isAuthenticated) {
        this.showError('CHANGE-PASSWORD.debug');
        return;
      }

      let id:string = this.authService.id;
      this.passwords.oldPassword = this.passwordsForm.controls['oldpassword'].value;
      this.passwords.newPassword = this.passwordsForm.controls['newpassword'].value;

      if (this.passwords.newPassword != this.passwordsForm.controls['newpasswordconf'].value) {
        this.showError('CHANGE-PASSWORD.pass-dont-match');
      } else if (this.passwords.newPassword == this.passwords.oldPassword) {
        this.showError('CHANGE-PASSWORD.pass-must-be-different');
      } else {
        this.userService.doChangePassword(id, this.passwords).subscribe(response => {
          
          this.showSuccess('CHANGE-PASSWORD.pass-changed');

        }, error => {

          if (error.error.globalError == 'Contraseña errónea') {
            this.showError('CHANGE-PASSWORD.incorrect-pass');
          } else {
            this.showError('CHANGE-PASSWORD.unk-changing-pass');
          }
        });
      }
    } else {
      if (this.passwordsForm.controls['oldpassword'].invalid) {
        if (this.passwordsForm.controls['oldpassword'].errors.required)
          this.showError('CHANGE-PASSWORD.empty-fields');
        else if (this.passwordsForm.controls['oldpassword'].value.length < 6)
          this.showErrorAux('CHANGE-PASSWORD.password-length', this.minLengthPass, 'CHANGE-PASSWORD.characters');
        else {
          this.showError('CHANGE-PASSWORD.unk-checking');
        }
      } else if (this.passwordsForm.controls['newpassword'].invalid) {
        if (this.passwordsForm.controls['newpassword'].errors.required)
          this.showError('CHANGE-PASSWORD.empty-fields');
        else if (this.passwordsForm.controls['newpassword'].value.length < 6)
          this.showErrorAux('CHANGE-PASSWORD.password-length', this.minLengthPass, 'CHANGE-PASSWORD.characters');
        else
          this.showError('CHANGE-PASSWORD.unk-checking');
      } else if (this.passwordsForm.controls['newpasswordconf'].invalid) {
        if (this.passwordsForm.controls['newpasswordconf'].errors.required)
          this.showError('CHANGE-PASSWORD.empty-fields');
        else if (this.passwordsForm.controls['newpasswordconf'].value.length < 6)
          this.showErrorAux('CHANGE-PASSWORD.password-length', this.minLengthPass, 'CHANGE-PASSWORD.characters');
        else
          this.showError('CHANGE-PASSWORD.unk-checking');
      }
    }

    this.passwordsForm.controls['oldpassword'].setValue('');
    this.passwordsForm.controls['newpassword'].setValue('');
    this.passwordsForm.controls['newpasswordconf'].setValue('');
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

  showErrorAux(message1: string, length: number, message2: string) {
    this.translate.get(message1).subscribe(res1 => {
      this.translate.get(message2).subscribe(res2 => {
        this.toastr.error('', res1 + length + res2);
      });
    });
  }
}
