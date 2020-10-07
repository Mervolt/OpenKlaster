import { Component, OnInit } from '@angular/core';
import {EndpointHolder} from '../model/EndpointHolder';

declare const SwaggerUIBundle: any;

@Component({
  selector: 'app-swagger-ui',
  templateUrl: './swagger-ui.component.html',
  styleUrls: ['./swagger-ui.component.css']
})
export class SwaggerUiComponent implements OnInit {

  ngOnInit(): void {
    const ui = SwaggerUIBundle({
      dom_id: '#swagger-ui',
      layout: 'BaseLayout',
      presets: [
        SwaggerUIBundle.presets.apis,
        SwaggerUIBundle.SwaggerUIStandalonePreset
      ],
      // Todo move it to config
      url: EndpointHolder.swaggerEndpoint,
      docExpansion: 'none',
      operationsSorter: 'alpha'
    });
  }

}
