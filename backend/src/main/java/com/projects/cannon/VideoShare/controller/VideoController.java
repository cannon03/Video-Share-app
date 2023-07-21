package com.projects.cannon.VideoShare.controller;

import com.projects.cannon.VideoShare.dto.UploadVideoResponse;
import com.projects.cannon.VideoShare.dto.VideoDto;
import com.projects.cannon.VideoShare.model.Video;
import com.projects.cannon.VideoShare.service.VideoService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file")MultipartFile file){
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file")MultipartFile file,@RequestParam("videoId") String videoId){
        return videoService.uploadThumbnail(file,videoId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetaData(@RequestBody VideoDto videoDto) {

        return videoService.editVideo(videoDto);

    }


    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId){

        return videoService.getVideoDetails(videoId);
    }
}
