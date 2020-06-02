import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToolboxService {

  constructor() { }

  getToolbox(toolbox: {
    logic: boolean,
    loops: boolean,
    math: boolean,
    text: boolean,
    lists: boolean,
    colors: boolean,
    variables: boolean,
    functions: boolean
  }): string {
    let toolboxString = this.beginning;

    if (toolbox.logic) {
      toolboxString += this.logic;
    }
    if (toolbox.loops) {
      toolboxString += this.loops;
    }
    if (toolbox.math) {
      toolboxString += this.math;
    }
    if (toolbox.text) {
      toolboxString += this.text;
    }
    if (toolbox.lists) {
      toolboxString += this.lists;
    }
    if (toolbox.colors) {
      toolboxString += this.colors;
    }
    if (toolbox.variables || toolbox.functions) {
      toolboxString += this.separation;
    }
    if (toolbox.variables) {
      toolboxString += this.variables;
    }
    if (toolbox.functions) {
      toolboxString += this.functions;
    }

    toolboxString += this.end;

    return toolboxString;
  }

  beginning: string = '<xml id="toolbox">';

  logic: string = '    <category name="Logic" colour="210">' +
      '      <category name="If" colour="170">' +
      '        <block type="controls_if"></block>' +
      '        <block type="controls_if">' +
      '          <mutation else="1"></mutation>' +
      '        </block>' +
      '        <block type="controls_if">' +
      '          <mutation elseif="1" else="1"></mutation>' +
      '        </block>' +
      '      </category>' +
      '      <category name="Boolean" colour="300">' +
      '        <block type="logic_compare"></block>' +
      '        <block type="logic_operation"></block>' +
      '        <block type="logic_negate"></block>' +
      '        <block type="logic_boolean"></block>' +
      '        <block type="logic_null"></block>' +
      '        <block type="logic_ternary"></block>' +
      '      </category>' +
      '    </category>';
  
  loops: string = '    <category name="Loops" colour="120">' +
      '      <block type="controls_repeat"></block>' +
      '      <block type="controls_repeat_ext">' +
      '        <value name="TIMES">' +
      '          <block type="math_number">' +
      '            <field name="NUM">10</field>' +
      '          </block>' +
      '        </value>' +
      '      </block>' +
      '      <block type="controls_whileUntil"></block>' +
      '      <block type="controls_for">' +
      '        <field name="VAR">i</field>' +
      '        <value name="FROM">' +
      '          <block type="math_number">' +
      '            <field name="NUM">1</field>' +
      '          </block>' +
      '        </value>' +
      '        <value name="TO">' +
      '          <block type="math_number">' +
      '            <field name="NUM">10</field>' +
      '          </block>' +
      '        </value>' +
      '        <value name="BY">' +
      '          <block type="math_number">' +
      '            <field name="NUM">1</field>' +
      '          </block>' +
      '        </value>' +
      '      </block>' +
      '      <block type="controls_forEach"></block>' +
      '      <block type="controls_flow_statements"></block>' +
      '    </category>';
  
  math: string = '    <category name="Math" colour="230">' +
      '      <block type="math_number">' +
      '        <field name="NUM">123</field>' +
      '      </block>' +
      '      <block type="math_arithmetic"></block>' +
      '      <block type="math_single"></block>' +
      '      <block type="math_trig"></block>' +
      '      <block type="math_constant"></block>' +
      '      <block type="math_number_property"></block>' +
      '      <block type="math_round"></block>' +
      '      <block type="math_on_list"></block>' +
      '      <block type="math_modulo"></block>' +
      '      <block type="math_constrain">' +
      '        <value name="LOW">' +
      '          <block type="math_number">' +
      '            <field name="NUM">1</field>' +
      '          </block>' +
      '        </value>' +
      '        <value name="HIGH">' +
      '          <block type="math_number">' +
      '            <field name="NUM">100</field>' +
      '          </block>' +
      '        </value>' +
      '      </block>' +
      '      <block type="math_random_int">' +
      '        <value name="FROM">' +
      '          <block type="math_number">' +
      '            <field name="NUM">1</field>' +
      '          </block>' +
      '        </value>' +
      '        <value name="TO">' +
      '          <block type="math_number">' +
      '            <field name="NUM">100</field>' +
      '          </block>' +
      '        </value>' +
      '      </block>' +
      '      <block type="math_random_float"></block>' +
      '    </category>';
  
  text: string = '    <category name="Text" colour="40">' +
      '      <block type="text_print">' +
      '        <value name="TEXT">' +
      '          <shadow type="text"></shadow>' +
      '        </value>' +
      '      </block>' +
      '      <block type="text"></block>' +
      '      <block type="text_join"></block>' +
      '      <block type="text_create_join_container"></block>' +
      '      <block type="text_create_join_item"></block>' +
      '      <block type="text_append">' +
      '        <value name="TEXT">' +
      '          <shadow type="text"></shadow>' +
      '        </value>' +
      '      </block>' +
      '      <block type="text_length"></block>' +
      '      <block type="text_isEmpty"></block>' +
      '      <block type="text_indexOf"></block>' +
      '      <block type="text_charAt"></block>' +
      '      <block type="text_getSubstring"></block>' +
      '      <block type="text_changeCase"></block>' +
      '      <block type="text_trim"></block>' +
      '      <block type="text_prompt_ext"></block>' +
      '      <block type="text_prompt"></block>' +
      '    </category>';
  
  lists: string = '    <category name="Lists" colour="20">' +
      '      <block type="lists_create_empty"></block>' +
      '      <block type="lists_create_with"></block>' +
      '      <block type="lists_create_with_container"></block>' +
      '      <block type="lists_create_with_item"></block>' +
      '      <block type="lists_repeat">' +
      '        <value name="NUM">' +
      '          <block type="math_number">' +
      '            <field name="NUM">5</field>' +
      '          </block>' +
      '        </value>' +
      '      </block>' +
      '      <block type="lists_length"></block>' +
      '      <block type="lists_isEmpty"></block>' +
      '      <block type="lists_indexOf"></block>' +
      '      <block type="lists_getIndex"></block>' +
      '      <block type="lists_setIndex"></block>' +
      '      <block type="lists_getSublist"></block>' +
      '      <block type="lists_sort"></block>' +
      '      <block type="lists_split"></block>' +
      '    </category>';
  
  colors: string = '    <category name="Color" colour="75">' +
      '      <block type="colour_picker"></block>' +
      '      <block type="colour_random"></block>' +
      '      <block type="colour_rgb"></block>' +
      '      <block type="colour_blend"></block>' +
      '    </category>';
  
  separation: string = '    <sep></sep>';
  
  variables: string = '    <category name="Variables" custom="VARIABLE" colour="330">' +
      '    </category>';
  
  functions: string = '    <category name="Functions" custom="PROCEDURE" colour="290">' +
      '    </category>';
  
  end: string = '</xml>';
}
