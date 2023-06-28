import { Injectable } from '@angular/core';
import { BaseService } from '@baseapp/base.service';
import { Observable } from 'rxjs';
import { ImportsApiConstants } from './imports.api-constants';
import { ImportErrorDetailsComponent } from './import-error-details/import-error-details.component';

@Injectable({
  providedIn: 'root'
})
export class ImportsService {

  getErrProtoTypingData(): Observable<any> {
    const subject:Observable<ImportErrorDetailsComponent> = new Observable(observer => {
      const data =  require('base/assets/static-sample-data/import-error-details.json');
      observer.next(data as ImportErrorDetailsComponent);
    });
    return subject;
}
  constructor(  public baseService:BaseService) { }

  initiateImport(...args: any):Observable<any>{
    const serviceOpts = ImportsApiConstants.create;
    const params= args[0];
    
    const subject = new Observable(observer => {
      this.baseService.post(serviceOpts,params).subscribe((response: any) => {
        observer.next(response);
      },
      (err:any) => {
        observer.error(err);
      });
    });

    return subject;
}
getErrorData(...args: any):Observable<any>{
    const serviceOpts = ImportsApiConstants.getErrorData;
    const params= args[0];
    
    const subject = new Observable(observer => {
      this.baseService.post(serviceOpts,params).subscribe((response: any) => {
        observer.next(response);
      },
      (err:any) => {
        observer.error(err);
      });
    });

    return subject;
}
getTemplateLink(...args: any):Observable<any>{
    const serviceOpts = ImportsApiConstants.getDownloadurl; 
    const params= args[0];
    
    const subject = new Observable(observer => {
      this.baseService.post(serviceOpts,params).subscribe((response: any) => {
        observer.next(response);
      },
      (err:any) => {
        observer.error(err);
      });
    });

    return subject;
}
update(...args: any):Observable<any>{
  const serviceOpts = ImportsApiConstants.update;
  const params= args[0];
  
  const subject = new Observable(observer => {
    this.baseService.put(serviceOpts,params).subscribe((response: any) => {
      observer.next(response);
    },
    (err:any) => {
      observer.error(err);
    });
  });

  return subject;
}

create(...args: any):Observable<any>{
  const serviceOpts = ImportsApiConstants.create;
  const params= args[0];
  
  const subject = new Observable(observer => {
    this.baseService.post(serviceOpts,params).subscribe((response: any) => {
      observer.next(response);
    },
    (err:any) => {
      observer.error(err);
    });
  });

  return subject;
}
getImportConfig(): Observable<any> {
  const subject:Observable<any> = new Observable(observer => {
    const data =  require('base/assets/import.json');
    observer.next(data as any);
  });
  return subject;
}


}

