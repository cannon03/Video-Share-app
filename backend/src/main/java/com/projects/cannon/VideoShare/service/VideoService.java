package com.projects.cannon.VideoShare.service;

import com.projects.cannon.VideoShare.dto.CommentDto;
import com.projects.cannon.VideoShare.dto.UploadVideoResponse;
import com.projects.cannon.VideoShare.dto.VideoDto;
import com.projects.cannon.VideoShare.model.Comment;
import com.projects.cannon.VideoShare.model.User;
import com.projects.cannon.VideoShare.model.Video;
import com.projects.cannon.VideoShare.repository.UserRepository;
import com.projects.cannon.VideoShare.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile){

        String videoUrl=s3Service.uploadFile(multipartFile);
        var video=new Video();
        video.setVideoUrl(videoUrl);

        var savedVideo = videoRepository.save(video);
        User currentUser=userService.getCurrentUser();
        currentUser.addToUploadedVideos(savedVideo.getId());
        userRepository.save(currentUser);

        return new UploadVideoResponse(savedVideo.getId(),savedVideo.getVideoUrl());

    }

    public VideoDto editVideo(VideoDto videoDto) {

        var savedVideo= getVideobyId(videoDto.getId());

        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        //savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl()); - bug.
        savedVideo.setVideoStatus(videoDto.getVideoStatus());



        videoRepository.save(savedVideo);
        return videoDto;

    }

    public String uploadThumbnail(MultipartFile file, String videoId) {

        var savedVideo = getVideobyId(videoId);

        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        Video video=videoRepository.save(savedVideo);

        return video.getThumbnailUrl();

    }

    Video getVideobyId(String videoId){

        return videoRepository.findById(videoId)
                .orElseThrow(()->new IllegalArgumentException("Cannot find video with id :"+videoId));

    }

    public VideoDto getVideoDetails(String videoId) {

        Video savedVideo = getVideobyId(videoId);

        increaseVideoCount(savedVideo);
        userService.addToVideoHistory(videoId);

        return mapToVideoDto(savedVideo);

    }


    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    public VideoDto likeVideo(String videoId) {
        Video videobyId= getVideobyId(videoId);

        if(userService.ifLikedVideo(videoId)){
            videobyId.decrementLikes();
            userService.removeFromLikedVIdeos(videoId);

        }else if(userService.ifdisLikedVideo(videoId)){
            videobyId.decrementdisLikes();
            userService.removeFromDislikedVideos(videoId);
            videobyId.incrementLikes();
            userService.addToLikedVideos(videoId);
        }else{
            videobyId.incrementLikes();
            userService.addToLikedVideos(videoId);
        }

        videoRepository.save(videobyId);

        //Create a method to return VideoDto.

        return mapToVideoDto(videobyId);

    }

    public VideoDto dislikeVideo(String videoId) {
        Video videobyId= getVideobyId(videoId);

        if(userService.ifdisLikedVideo(videoId)){
            videobyId.decrementdisLikes();
            userService.removeFromDislikedVideos(videoId);

        }else if(userService.ifLikedVideo(videoId)){
            videobyId.decrementLikes();
            userService.removeFromLikedVIdeos(videoId);
            videobyId.incrementdisLikes();
            userService.addToDislikedVideos(videoId);
        }else{
            videobyId.incrementdisLikes();
            userService.addToDislikedVideos(videoId);
        }

        videoRepository.save(videobyId);

        //Create a method to return VideoDto.

        return mapToVideoDto(videobyId);

    }
    private VideoDto mapToVideoDto(Video videobyId) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(videobyId.getVideoUrl());
        videoDto.setThumbnailUrl(videobyId.getThumbnailUrl());
        videoDto.setId(videobyId.getId());
        videoDto.setTitle(videobyId.getTitle());
        videoDto.setDescription(videobyId.getDescription());
        videoDto.setTags(videobyId.getTags());
        videoDto.setVideoStatus(videobyId.getVideoStatus());
        videoDto.setLikeCount(videobyId.getLikes().get());
        videoDto.setDislikeCount(videobyId.getDislikes().get());
        videoDto.setViewCount(videobyId.getViewCount().get());
        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        Video video = getVideobyId(videoId);
        Comment comment= new Comment();
        comment.setText(commentDto.getCommentText());
        comment.setAuthorId(commentDto.getAuthorId());
        video.addComment(comment);

        videoRepository.save(video);
    }

    public List<CommentDto> getAllComments(String videoId) {
        Video video= getVideobyId(videoId);
        List<Comment> commentList=video.getCommentsList();

        return commentList.stream().map(this::maptoCommentDto).toList();
    }

    private CommentDto maptoCommentDto(Comment comment) {

        CommentDto commentDto=new CommentDto();
        commentDto.setCommentText(comment.getText());
        commentDto.setAuthorId(commentDto.getAuthorId());

        return commentDto;

    }

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream().map(this::mapToVideoDto).toList();
    }
}
