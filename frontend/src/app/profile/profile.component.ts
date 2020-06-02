import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';

import { AuthService } from '../auth.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private toastr: ToastrService,
    private translate: TranslateService) { }

  username = this.authService.userName;
  signUpDate: string;
  role: string;
  user =  {
    email: "",
    firstName: "",
    id: +this.authService.id,
    lastName: ""
  };

  editingFirstName: boolean = false;
  firstNameInput = new FormControl('', Validators.required);
  editingLastName: boolean = false;
  lastNameInput = new FormControl('', Validators.required);
  editingEmail: boolean = false;
  emailInput = new FormControl('', [Validators.required, Validators.email]);

  updateData(info: any) {
    this.signUpDate = new Date(info.signUpDate).toLocaleString();
    this.role = info.role;
    this.user.firstName = info.firstName;
    this.user.lastName = info.lastName;
    this.user.email = info.email;
    localStorage.setItem('firstname', info.firstName);
  }

  initForm(info: any) {
    this.firstNameInput.setValue(info.firstName);
    this.lastNameInput.setValue(info.lastName);
    this.emailInput.setValue(info.email);
  }

  ngOnInit() {
    this.userService.getUserInfo(this.authService.id).subscribe(response => {
      this.updateData(response);
      this.initForm(response);
    }, error => {
      this.showError("PROFILE.unk-error");
    });
  }

  editEmail() {
    this.editingEmail = true;
  }

  stopEditingEmail() {
    this.editingEmail = false;
  }

  updateEmail() {
    if (this.emailInput.invalid) {
      if (this.emailInput.errors.required) {
        this.showError('PROFILE.empty-email');
        return;
      }
      if (this.emailInput.errors.email) {
        this.showError('PROFILE.email-invalid');
        return;
      }
    }

    this.editingEmail = false;

    if (this.user.email != this.emailInput.value) {
      let auxEmail = this.user.email;
      this.user.email = this.emailInput.value;
      this.userService.doUpdateProfile(this.user).subscribe(response => {
        this.updateData(response);
        this.showSuccess('PROFILE.email-updated');
      }, error => {
        this.user.email = auxEmail;
        this.showError('PROFILE.error-updating-email');
      });
    }
  }

  editFirstName() {
    this.editingFirstName = true;
  }

  stopEditingFirstName() {
    this.editingFirstName = false;
  }

  updateFirstName() {
    if (this.firstNameInput.invalid) {
      if (this.firstNameInput.errors.required) {
        this.showError('PROFILE.empty-name');
        return;
      }
    }

    this.editingFirstName = false;
    
    if (this.user.firstName != this.firstNameInput.value) {
      let auxFirstName = this.user.firstName;
      this.user.firstName = this.firstNameInput.value;
      this.userService.doUpdateProfile(this.user).subscribe(response => {
        this.updateData(response);
        this.showSuccess('PROFILE.first-name-updated');
      }, error => {
        this.user.firstName = auxFirstName;
        this.showError('PROFILE.error-updating-first-name');
      });
    }
  }

  editLastName() {
    this.editingLastName = true;
  }

  stopEditingLastName() {
    this.editingLastName = false;
  }

  updateLastName() {
    if (this.lastNameInput.invalid) {
      if (this.lastNameInput.errors.required) {
        this.showError('PROFILE.empty-last-name');
        return;
      }
    }

    this.editingLastName = false;

    if (this.user.lastName != this.lastNameInput.value) {
      let auxLastName = this.user.lastName;
      this.user.lastName = this.lastNameInput.value;
      this.userService.doUpdateProfile(this.user).subscribe(response => {
        this.updateData(response);
        this.showSuccess('PROFILE.last-name-updated');
      }, error => {
        this.user.lastName = auxLastName;
        this.showError('PROFILE.error-updating-last-name');
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
