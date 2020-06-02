import { BrowserModule } from '@angular/platform-browser';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularFontAwesomeModule } from 'angular-font-awesome';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AuthenticationComponent } from './authentication/authentication.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { RegisterComponent } from './register/register.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ProfileComponent } from './profile/profile.component';
import { BlocklyComponent } from './blockly/blockly.component';
import { BlocklyAuxService } from './blockly-aux.service';
import { HighlightModule } from 'ngx-highlightjs';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';

import javascript from 'highlight.js/lib/languages/javascript';
import python from 'highlight.js/lib/languages/python';
import { MyprogramsComponent } from './myprograms/myprograms.component';
import { EditorComponent } from './editor/editor.component';
import { MygroupsComponent } from './mygroups/mygroups.component';
import { GroupdetailComponent } from './groupdetail/groupdetail.component';
import { ExploreComponent } from './explore/explore.component';
import { UserComponent } from './user/user.component';
import { ProgramPermissionsComponent } from './program-permissions/program-permissions.component';
import { GroupMembersComponent } from './group-members/group-members.component';
import { ExerciseComponent } from './exercise/exercise.component';
import { NotFoundComponent } from './not-found/not-found.component';

export const t = window['Blockly'];

export function hljsLanguages() {
  return [
    {name: 'javascript', func: javascript},
    {name: 'python', func: python}
  ];
}

@NgModule({
  declarations: [
    AppComponent,
    AuthenticationComponent,
    WelcomeComponent,
    RegisterComponent,
    ChangePasswordComponent,
    ProfileComponent,
    BlocklyComponent,
    MyprogramsComponent,
    EditorComponent,
    MygroupsComponent,
    GroupdetailComponent,
    ExploreComponent,
    UserComponent,
    ProgramPermissionsComponent,
    GroupMembersComponent,
    ExerciseComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    AngularFontAwesomeModule,
    FormsModule,
    ToastrModule.forRoot({
      closeButton: true,
      positionClass: 'toast-top-right',
      timeOut: 2500
    }),
    HighlightModule.forRoot({
      languages: hljsLanguages
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (http: HttpClient) => {
          return new TranslateHttpLoader(http);
        },
        deps: [ HttpClient ]
      }
    })
  ],
  schemas: [NO_ERRORS_SCHEMA],
  providers: [
    { provide: BlocklyAuxService, useFactory: () => {
      return new BlocklyAuxService(t); 
    } 
  }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}