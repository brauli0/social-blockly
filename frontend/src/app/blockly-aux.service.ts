import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BlocklyAuxService {

  constructor(public blockly: any) { }
}
