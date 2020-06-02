import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FormControl, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { AuthService } from '../auth.service';
import { ProgramService }  from '../program.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-myprograms',
  templateUrl: './myprograms.component.html',
  styleUrls: ['./myprograms.component.css']
})
export class MyprogramsComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private programService: ProgramService,
    private toastr: ToastrService,
    private router: Router,
    private translate: TranslateService) {}

  myprograms: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
      id: number,
      userName: string,
      firstName: string,
      lastName: string,
      email: string,
      signUpDate: string,
      role: string
    }[]
  }[] = [];

  pageConfig: {
    elementsByPage: number,
    size: number,
    hasElements: boolean,
    firstShown: number,
    lastShown: number
  } = {elementsByPage: 0, size: 0, hasElements: false, firstShown: 0, lastShown: 0};

  pageShowingPrograms: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
      id: number,
      userName: string,
      firstName: string,
      lastName: string,
      email: string,
      signUpDate: string,
      role: string
    }[]
  }[] = [];

  programNameInput = new FormControl('', Validators.required);
  programDescInput = new FormControl('');

  configPagination() {
    this.pageConfig.elementsByPage = environment.elementsByPage;
    this.pageConfig.size = this.myprograms.length;
    this.pageConfig.hasElements = this.pageConfig.size > 0;
    if (this.pageConfig.hasElements) {
      this.pageConfig.firstShown = 1;
      this.pageConfig.lastShown =
        this.pageConfig.size > this.pageConfig.elementsByPage ? this.pageConfig.elementsByPage : this.pageConfig.size;
    } else {
      this.pageConfig.firstShown = 0;
      this.pageConfig.lastShown = 0;
    }
    
    this.pageShowingPrograms = [];
    if (this.pageConfig.hasElements) {
      for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
        this.pageShowingPrograms.push(this.myprograms[i]);
      }
    }
  }

  previousPage() {
    if (this.pageConfig.firstShown < 2) {
      return;
    }

    this.pageConfig.lastShown = this.pageConfig.firstShown - 1;
    this.pageConfig.firstShown -= this.pageConfig.elementsByPage;

    this.pageShowingPrograms = [];
    for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
      this.pageShowingPrograms.push(this.myprograms[i]);
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

    this.pageShowingPrograms = [];
    for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
      this.pageShowingPrograms.push(this.myprograms[i]);
    }
  }

  getAllPrograms() {
    this.myprograms = [];
    this.programService.getProgramsByUser(+this.authService.id).subscribe(response => {
      response.reverse();
      response.forEach(element => {
        this.myprograms.push(
          {
            id: element.id,
            userId: element.userId,
            programName: element.programName,
            programDesc: element.programDesc,
            creationDate: new Date(element.creationDate).toLocaleString(),
            updateDate: new Date(element.updateDate).toLocaleString(),
            code: element.code,
            privateProgram: element.privateProgram,
            visibility: element.visibility,
            sharedUsers: element.sharedUsers
          }
        );
      });

      this.configPagination();
    }, error => {
      console.log(error);
      this.showError('PROGRAMS.unk-error');
    });
  }

  ngOnInit() {
    this.getAllPrograms();
  }

  // SAVE
  save() {
    if (this.programNameInput.invalid) {
      if (this.programNameInput.errors.required) {
        this.showError('PROGRAMS.empty-name');
        return;
      }
    }

    let program = {
      userId: +this.authService.id,
      programName: this.programNameInput.value,
      programDesc: this.programDescInput.value,
      code: '',
      privateProgram: environment.newProgramPrivateByDefault
    }
    this.programService.createProgram(program).subscribe(response => {
      this.showSuccess('PROGRAMS.created');
      this.router.navigate(['/program/' + response.id]);
    }, error => {
      console.log(error);
      this.showError('PROGRAMS.error-creating');
    });
  }

  // EDIT
  changeModal(name: string, desc: string) {
    this.programNameInput.setValue(name);
    this.programDescInput.setValue(desc);
  }

  private updateProgram(id: number, code: string) {
    let program = {
      userId: +this.authService.id,
      programId: id,
      programName: this.programNameInput.value,
      programDesc: this.programDescInput.value,
      code: code
    }

    this.programService.updateProgram(program).subscribe(response => {
      this.showSuccess('PROGRAMS.updated');
      this.getAllPrograms();
    }, error => {
      console.log(error);
      this.showError('PROGRAMS.error-updating');
    });
  }

  edit(id: number, programName: string, programDesc: string, code: string, privateProgram: boolean) {
    if (this.programNameInput.invalid) {
      if (this.programNameInput.errors.required) {
        this.showError('PROGRAMS.empty-name');
        return;
      }
    }

    // Checking if something has changed
    if (!(programName == this.programNameInput.value && programDesc == this.programDescInput.value)) {
      this.updateProgram(id, code);
    }
  }

  // DELETE
  delete(id: number) {
    this.programService.deleteProgram({userId: +this.authService.id, programId: id}).subscribe(response => {
      this.showSuccess('PROGRAMS.deleted');
      this.getAllPrograms();
    }, error => {
      console.log(error);
      this.showError('PROGRAMS.error-deleting');
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
