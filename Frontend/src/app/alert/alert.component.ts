import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnChanges {
  @Input() message: string = ""; // Cambia 'String' a 'string'
  showAlert: boolean = false;

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['message'] && this.message) {
      this.showAlert = true;
      setTimeout(() => {
        this.showAlert = false;
      }, 3000);
    }
  }
}
