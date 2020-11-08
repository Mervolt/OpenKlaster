import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-description-tile',
  templateUrl: './description-tile.component.html',
  styleUrls: ['./description-tile.component.css']
})
export class DescriptionTileComponent implements OnInit {

  @Input() description: string
  @Input() tileTitle: string
  @Input() icon: string

  constructor() { }

  ngOnInit(): void {
  }

}
