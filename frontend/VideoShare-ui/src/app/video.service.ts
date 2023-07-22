import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UploadVideoComponent} from "./upload-video/upload-video.component";
import {UploadVIdeoResponse} from "./upload-video/UploadVIdeoResponse";
import {Observable} from "rxjs";
import {VideoDto} from "./video-dto";

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private httpClient:HttpClient) { }

  uploadVideo(fileEntry:File):Observable<UploadVIdeoResponse>{

    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name)
    return this.httpClient.post<UploadVIdeoResponse>("http://localhost:8080/api/videos",formData);

  }

  uploadThumbnail(fileEntry:File,videoId:string):Observable<string>{

    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name)
    formData.append('videoId',videoId);
    return this.httpClient.post("http://localhost:8080/api/videos/thumbnail",formData,{
      responseType : 'text'
    });
  }
  getVideo(videoId:string):Observable<VideoDto>{
    console.log("https://localhost:8080/api/videos/"+videoId)
    return this.httpClient.get<VideoDto>("http://localhost:8080/api/videos/"+videoId);
  }

  saveVideo(videoMetaData: VideoDto):Observable<VideoDto> {

    return this.httpClient.put<VideoDto>("http://localhost:8080/api/videos",videoMetaData);
  }
  getAllVideos():Observable<Array<VideoDto>>{
    return this.httpClient.get<Array<VideoDto>>("http://localhost:8080/api/videos");
  }

  likeVideo(videoId: string):Observable<VideoDto> {

    return this.httpClient.post<VideoDto>("http://localhost:8080/api/videos/"+videoId+"/like",null)

  }
  dislikeVideo(videoId: string):Observable<VideoDto> {

    return this.httpClient.post<VideoDto>("http://localhost:8080/api/videos/"+videoId+"/dislike",null)

  }
}
