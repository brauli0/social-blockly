import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';

import { BlocklyAuxService } from '../blockly-aux.service';
import { ToolboxService } from '../toolbox.service';
import { ProgramService } from '../program.service';
import { ExerciseService } from '../exercise.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-blockly',
  templateUrl: './blockly.component.html',
  styleUrls: ['./blockly.component.css']
})
export class BlocklyComponent implements OnInit {

  constructor(
    private blocklyAuxService: BlocklyAuxService,
    private programService: ProgramService,
    private exerciseService: ExerciseService,
    private authService: AuthService,
    private toastr: ToastrService,
    private toolboxService: ToolboxService,
    private route: ActivatedRoute,
    private translate: TranslateService) { }

  workspace: any;
  toolbox: string;
  programId: number;

  programName: string;
  jscode: string;
  pycode: string;
  showcode: string;
  showingjs: boolean = false;
  showSaveButton: boolean = false;

  myUserId: number;

  program: {
    id: number,
    userId: number,
    exerciseId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string
  };

  oldXmlCode: string;

  saved: boolean;

  ngOnInit() {
    this.myUserId = +this.authService.id;
    this.programId = +this.route.snapshot.paramMap.get('id');

    if (this.programId != 0) {
      this.programService.getProgram(this.programId).subscribe(response => {
        this.program = response;
        this.oldXmlCode = this.program.code;
        this.saved = true;
        this.showSaveButton = this.program.userId == this.myUserId ? true : false;

        if (this.program.exerciseId != undefined) {
          this.exerciseService.getExercise(this.program.exerciseId).subscribe(response => {
            let toolboxCode = response.blocks;
            this.setToolbox(toolboxCode);
            this.initBlockly();
            this.setBlocksFromXml(this.program.code);
          }, error => {
            console.log(error);
            this.showError('EXERCISE.unk-error');
          });
        } else {
          this.setToolbox('YYYYYYYY');
          this.initBlockly();
          this.setBlocksFromXml(this.program.code);
        }

      }, error => {
        console.log(error);
      });
    } else {
      let exampleProgramId = 1;
      this.programService.getExampleProgram().subscribe(response => {
        this.setToolbox('YYYYYYYY');
        this.initBlockly();
        this.program = response;
        this.setBlocksFromXml(this.program.code);
        this.showSaveButton = false;
      }, error => {
        console.log(error);
      });
    }
  }

  setToolbox(toolboxCode: string) {
    let codeArray: string[] = toolboxCode.split('');

    this.toolbox = this.toolboxService.getToolbox({
      logic: codeArray[0] == 'Y',
      loops: codeArray[1] == 'Y',
      math: codeArray[2] == 'Y',
      text: codeArray[3] == 'Y',
      lists: codeArray[4] == 'Y',
      colors: codeArray[5] == 'Y',
      variables: codeArray[6] == 'Y',
      functions: codeArray[7] == 'Y'
    });
  }

  initBlockly() {
    this.workspace = this.blocklyAuxService.blockly.inject(
      'blockly-div',
      {
        toolbox: this.toolbox,
        grid:
          {spacing: 25,
            length: 2,
            colour: '#28a745',
            snap: false},
        zoom:
          {controls: true,
           wheel: false,
           startScale: 1.0,
           maxScale: 3,
           minScale: 0.3,
           scaleSpeed: 1.2},
        trashcan: true
      }
    );
    
    this.workspace.addChangeListener(this.updateCode.bind(this));
  }

  setBlocksFromXml(xmlText: string) {
    let xml = this.blocklyAuxService.blockly.Xml.textToDom(xmlText);
    this.blocklyAuxService.blockly.Xml.domToWorkspace(xml, this.workspace);
  }

  updateCode(event) {
    this.getJSCode();
    this.getPythonCode();
    if (this.showingjs)
      this.showcode = this.jscode;
    else
      this.showcode = this.pycode;
    
    this.saved = this.oldXmlCode == this.getXmlCode();
  }

  showJs() {
    this.showingjs = true;
    this.showcode = this.jscode;
  }

  showPy() {
    this.showingjs = false;
    this.showcode = this.pycode;
  }

  getXmlCode(): string {
    let xml_text = this.blocklyAuxService.blockly.Xml.domToText(
      this.blocklyAuxService.blockly.Xml.workspaceToDom(this.workspace)
    );
    return xml_text;
  }

  getJSCode(): string {
    let js = this.blocklyAuxService.blockly.JavaScript.workspaceToCode(this.workspace)
    this.jscode = js;
    return js;
  }

  getPythonCode(): string {
    let py = this.blocklyAuxService.blockly.Python.workspaceToCode(this.workspace);
    this.pycode = py;
    return py;
  }

  clearWorkspace() {
    this.workspace.clear();
  }

  undo(redo: boolean) {
    this.workspace.undo(redo);
  }

  clearOutput() {
    let code = 'var outputInHTML = document.getElementById("outputInHTML");' +
    'outputInHTML.textContent = ""';

    try {
      eval(code);
    } catch (e) {
      console.log(e);
    }
  }

  executeCode() {
    // Change the output of the program
    let code = this.getJSCode();
    let splitted = code.split('window.alert(');
    let splitted2: string[];
    let codeAux = splitted[0];
    splitted.shift();
    splitted.forEach(element => {
      codeAux += 'outputInHTML.textContent += ';
      splitted2 = element.split(');');
      codeAux += splitted2[0];
      codeAux += ' + "\\n";';
      splitted2.shift();
      splitted2.forEach(element => {
        codeAux += element;
      });
    });

    try {
      eval(codeAux);
    } catch (e) {
      console.log(e);
    }

    this.scroll();
  }

  save() {
    let auxProgram = {
      programId: this.program.id,
      userId: this.program.userId,
      programName: this.program.programName,
      programDesc: this.program.programDesc,
      creationDate: this.program.creationDate,
      updateDate: this.program.updateDate,
      code: this.getXmlCode()
    }
    this.programService.updateProgram(auxProgram).subscribe(response => {
      this.showSuccess('BLOCKLY.program-updated');
      this.oldXmlCode = this.getXmlCode();
      this.saved = true;
    }, error => {
      console.log(error);
      this.showError('BLOCKLY.error-updating');
    });;
  }

  private scroll() {
    let scrollcode = 'async function sleep() {' +
      'return new Promise(resolve => setTimeout(resolve, 200));' +
    '}' +
    'async function f() {'+
    'await sleep();'+
    'var scrolldiv = document.getElementById("output-pre");' +
    'scrolldiv.scrollTop = scrolldiv.scrollHeight;' +
    '}' +
    'f()';

    try {
      eval(scrollcode);
    } catch (e) {
      console.log(e);
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