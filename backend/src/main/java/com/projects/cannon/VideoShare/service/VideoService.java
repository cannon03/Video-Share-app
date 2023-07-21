package com.projects.cannon.VideoShare.service;

import com.projects.cannon.VideoShare.dto.UploadVideoResponse;
import com.projects.cannon.VideoShare.dto.VideoDto;
import com.projects.cannon.VideoShare.model.Video;
import com.projects.cannon.VideoShare.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile){

        String videoUrl=s3Service.uploadFile(multipartFile);
        var video=new Video();
        video.setVideoUrl(videoUrl);

        var savedVideo = videoRepository.save(video);

        return new UploadVideoResponse(savedVideo.getId(),savedVideo.getVideoUrl());

    }

    public VideoDto editVideo(VideoDto videoDto) {

        var savedVideo= getVideobyId(videoDto.getId());

        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        videoRepository.save(savedVideo);
        return videoDto;

    }

    public String uploadThumbnail(MultipartFile file, String videoId) {

        var savedVideo = getVideobyId(videoId);

        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        videoRepository.save(savedVideo);

        return thumbnailUrl;

    }

    Video getVideobyId(String videoId){

       return videoRepository.findById(videoId)
                .orElseThrow(()->new IllegalArgumentException("Cannot find video with id :"+videoId));

    }

    public VideoDto getVideoDetails(String videoId) {

        Video savedVideo = getVideobyId(videoId);
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());

        return videoDto;
    }
}
