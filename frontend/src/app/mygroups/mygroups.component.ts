import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FormControl, Validators } from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';

import { AuthService } from '../auth.service';
import { GroupService }  from '../group.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-mygroups',
  templateUrl: './mygroups.component.html',
  styleUrls: ['./mygroups.component.css']
})
export class MygroupsComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private groupService: GroupService,
    private toastr: ToastrService,
    private router: Router,
    private translate: TranslateService) { }

  mygroups: {
    id: number,
    groupName: string,
    creationDate: string
  }[] = [];

  groupNameInput = new FormControl('', Validators.required);

  pageConfig: {
    elementsByPage: number,
    size: number,
    hasElements: boolean,
    firstShown: number,
    lastShown: number
  } = {elementsByPage: 0, size: 0, hasElements: false, firstShown: 0, lastShown: 0};

  pageShowingGroups: {
    id: number,
    groupName: string,
    creationDate: string
  }[] = [];

  configPagination() {
    this.pageConfig.elementsByPage = environment.elementsByPage;
    this.pageConfig.size = this.mygroups.length;
    this.pageConfig.hasElements = this.pageConfig.size > 0;
    if (this.pageConfig.hasElements) {
      this.pageConfig.firstShown = 1;
      this.pageConfig.lastShown =
        this.pageConfig.size > this.pageConfig.elementsByPage ? this.pageConfig.elementsByPage : this.pageConfig.size;
    } else {
      this.pageConfig.firstShown = 0;
      this.pageConfig.lastShown = 0;
    }
    
    this.pageShowingGroups = [];
    if (this.pageConfig.hasElements) {
      for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
        this.pageShowingGroups.push(this.mygroups[i]);
      }
    }
  }

  previousPage() {
    if (this.pageConfig.firstShown < 2) {
      return;
    }

    this.pageConfig.lastShown = this.pageConfig.firstShown - 1;
    this.pageConfig.firstShown -= this.pageConfig.elementsByPage;

    this.pageShowingGroups = [];
    for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
      this.pageShowingGroups.push(this.mygroups[i]);
    }
  }

  nextPage() {
    if (this.pageConfig.lastShown == this.pageConfig.size) {
      return;
    }

    this.pageConfig.firstShown = this.pageConfig.lastShown + 1;
    
    if ((this.pageConfig.lastShown + this.pageConfig.elementsByPage) < this.pageConfig.size) {
      this.pageConfig.lastShown += this.pageConfig.elementsByPage;
    } else {
      this.pageConfig.lastShown = this.pageConfig.size;
    }

    this.pageShowingGroups = [];
    for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
      this.pageShowingGroups.push(this.mygroups[i]);
    }
  }

  getAllGroups() {
    this.mygroups = [];
    this.groupService.getAllGroupsByUser(+this.authService.id).subscribe(response => {
      response.forEach(element => {
        this.mygroups.push({
          id: element.id,
          groupName: element.groupName,
          creationDate: new Date(element.creationDate).toLocaleString()
        });
      });

      this.configPagination();
    }, error => {
      console.log(error);
      this.showError('GROUPS.unk-error');
    });
  }

  ngOnInit() {
    this.getAllGroups();
  }

  save() {
    if (this.groupNameInput.invalid) {
      if (this.groupNameInput.errors.required) {
        this.showError('GROUPS.empty-name');
        return;
      }
    }

    this.groupService.createGroup({
      userId: +this.authService.id,
      groupName: this.groupNameInput.value
    }).subscribe(response => {
      this.showSuccess('GROUPS.created');
      this.router.navigate(['/group/' + response.id]);
    }, error => {
      console.log(error);
      if (error.error.globalError == 'operación no permitida') {
        this.showError('GROUPS.error-permissions');
      } else {
        this.showError('GROUPS.error-creating');
      }
    });
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
