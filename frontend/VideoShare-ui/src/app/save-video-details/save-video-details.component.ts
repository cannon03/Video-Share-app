import {Component, inject} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MatChipEditedEvent, MatChipInputEvent} from "@angular/material/chips";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {ActivatedRoute} from "@angular/router";
import {VideoService} from "../video.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {VideoDto} from "../video-dto";

@Component({
  selector: 'app-save-video-details',
  templateUrl: './save-video-details.component.html',
  styleUrls: ['./save-video-details.component.css']
})

export class SaveVideoDetailsComponent {



  saveVideodetailsF : FormGroup;
  title : FormControl =new FormControl('');
  description : FormControl =new FormControl('');
  videoStatus : FormControl =new FormControl('');


  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  tags: string[] = [];

  announcer = inject(LiveAnnouncer);
  selectedFile!:File;
  selectedFileName = '';
  videoId='';
  fileSelected=false;
  videoUrl!:string;
  thumbnailUrl!:string;

  constructor(private activatedRoute: ActivatedRoute,private videoService : VideoService,
              private matSnackBar : MatSnackBar) {
    this.videoId= this.activatedRoute.snapshot.params['videoId'];
    this.saveVideodetailsF = new FormGroup({

      title: this.title,
      description: this.description,
      videoStatus: this.videoStatus,
    })
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our tag
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(tag: string): void {
    const index = this.tags.indexOf(tag);

    if (index >= 0) {
      this.tags.splice(index, 1);

      this.announcer.announce(`Removed ${tag}`);
    }
  }

  edit(tag: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    // Remove tag if it no longer has a name
    if (!value) {
      this.remove(tag);
      return;
    }

    // Edit existing tag
    const index = this.tags.indexOf(tag);
    if (index >= 0) {
      this.tags[index] = value;
    }
  }


  onFileSelected($event: Event) {

    // @ts-ignore
    this.selectedFile = $event.target.files[0]
    this.selectedFileName = this.selectedFile.name;
    this.fileSelected=true;

  }

  onUpload() {

    this.videoService.uploadThumbnail(this.selectedFile,this.videoId)
      .subscribe(data =>{
        console.log(data);
        this.matSnackBar.open("Thumbnail Upload Successful","OK");
      })


  }

  saveVideo() {
    this.setVideoMetaData();
    const videoMetaData:VideoDto={
      "id":this.videoId,
      "title":this.saveVideodetailsF.get('title')?.value,
      "description":this.saveVideodetailsF.get('description')?.value,
      "tags":this.tags,
      "videoStatus":this.saveVideodetailsF.get('videoStatus')?.value,
      "videoUrl":this.videoUrl,
      "thumbnailUrl":this.thumbnailUrl,
      "likeCount":0,
      "dislikeCount":0,
      "viewCount":0,
    }
    this.videoService.saveVideo(videoMetaData).subscribe(data=>{
      console.log("Inside save video: "+ data.thumbnailUrl);
      this.matSnackBar.open("Video Metadata Updated successfully","OK");
    });
  }

  setVideoMetaData(){
    this.videoService.getVideo(this.videoId).subscribe(data =>{
      this.videoUrl=data.videoUrl;
      console.log("Inside get video: "+data.thumbnailUrl);
      this.thumbnailUrl=data.thumbnailUrl;
    })
    this.saveVideodetailsF = new FormGroup({

      title:this.title,
      description:this.description,
      videoStatus:this.videoStatus,
    })
  }

}
