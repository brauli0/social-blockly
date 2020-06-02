import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';

import { AuthService} from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor (
    private router: Router,
    public authService: AuthService,
    private toastr: ToastrService,
    private translate: TranslateService) {
      translate.setDefaultLang('en');
      translate.use('gl');
    }
  doLogout() {
    this.authService.logout();
    this.showSuccess('Logout!');
    this.router.navigate(['/login']);
  }

  showSuccess(message: string) {
    this.toastr.success('', message);
  }
}
