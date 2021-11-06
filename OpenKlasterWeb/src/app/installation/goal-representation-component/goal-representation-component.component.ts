import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-goal-representation-component',
  templateUrl: './goal-representation-component.component.html',
  styleUrls: ['./goal-representation-component.component.css']
})
export class GoalRepresentationComponentComponent implements OnInit {
  @Input() goalAmount: number;
  @Input() currentAmount: number;

  constructor() {
  }

  ngOnInit(): void {
  }

  countAmount() {
    if (this.goalAmount === undefined || this.currentAmount === undefined) {
      return 0;
    }
    else {
      return this.currentAmount / this.goalAmount * 100;
    }
  }
}
