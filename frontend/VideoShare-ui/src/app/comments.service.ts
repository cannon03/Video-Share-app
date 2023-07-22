import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CommentDto} from "./comment-dto";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  constructor(private httpClient:HttpClient) { }

  postComment(commentDto:any, videoId:string){
    return this.httpClient.post<any>("http://localhost:8080/api/videos/"+videoId+"/comment",commentDto);
  }

  getAllComments(videoId: string):Observable<Array<CommentDto>> {

    return this.httpClient.get<CommentDto[]>("http://localhost:8080/api/videos/"+videoId+"/comment");

  }
}
