import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userId:string='';

  constructor(private httpClient:HttpClient) { }

  subscribeToUser(userId:string):Observable<boolean>{
    return this.httpClient.post<boolean>("http://localhost:8080/api/user/subscribe/"+userId,null);
  }
  unSubscribeToUser(userId:string):Observable<boolean>{
    console.log(userId);
    return this.httpClient.post<boolean>("http://localhost:8080/api/user/unsubscribe/"+userId,null);
  }


  registerUser() {
    return this.httpClient.get("http://localhost:8080/api/user/register",{responseType:"text"})
      .subscribe(data=>{
      console.log("Id: "+ data);
      this.userId=data;
    })
  }
  getUserId()
  {
    return this.userId;
  }
}
