import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {VideoService} from "../video.service";
import {UserService} from "../user.service";

@Component({
  selector: 'app-video-detail',
  templateUrl: './video-detail.component.html',
  styleUrls: ['./video-detail.component.css']
})
export class VideoDetailComponent {

  userId!:string;
  videoId!:string;
  videoUrl!:string;
  videoTitle!:string;
  videoDescription!:string;
  tags:Array<string>=[];
  videoAvailiable:boolean=false;
  likeCount:number=0;
  dislikeCount:number=0;
  viewCount:number=0;
  showSubscribeButton:boolean=true;
  showUnsubscribeButton:boolean=false;
  constructor(private activatedRoute:ActivatedRoute,
              private videoService:VideoService,
              private userService:UserService) {
    this.videoId= this.activatedRoute.snapshot.params['videoId'];
    this.userId=userService.getUserId();
    console.log("User id at video details :"+ this.userId);

    this.videoService.getVideo(this.videoId).subscribe(data =>{
      this.videoUrl=data.videoUrl;
      this.videoTitle= data.title;
      this.videoDescription= data.description;
      this.tags= data.tags;
      this.videoAvailiable=true;
      this.likeCount=data.likeCount;
      this.dislikeCount=data.dislikeCount;
      this.viewCount=data.viewCount;
      this.videoAvailiable=true;
    })
  }

  likeVideo() {
    this.videoService.likeVideo(this.videoId).subscribe(data=>{
      this.likeCount=data.likeCount;
      this.dislikeCount=data.dislikeCount;
    })
  }
  dislikeVideo() {
    this.videoService.dislikeVideo(this.videoId).subscribe(data=>{
      this.likeCount=data.likeCount;
      this.dislikeCount=data.dislikeCount;
    })
  }

  unSubscribeToUser() {
    let userId=this.userService.getUserId();
    this.userService.unSubscribeToUser(userId).subscribe(data=>{
      this.showSubscribeButton=true;
      this.showUnsubscribeButton=false;
    });
  }

  subscribeToUser() {
    let userId=this.userService.getUserId();
    this.userService.subscribeToUser(userId).subscribe(data=>{
      this.showSubscribeButton=false;
      this.showUnsubscribeButton=true;
    });
  }
}
