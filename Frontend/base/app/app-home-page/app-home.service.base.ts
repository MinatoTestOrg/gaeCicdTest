import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppHomeBaseService {

  

  config: any = [{
    "data": {},
    "children": [],
    "expanded": true,
    "folder": true,
    "key": "landingPage",
    "title": "Landing Page",
    "type": "landingPage",
    "selected": false
  }];

  public getLandingPageData() {
	  const data: any = (this.config.find((t: { type: string; }) => t.type === "page"));
	  return data.children[0] || {};
	}

}