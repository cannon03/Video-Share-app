package com.projects.cannon.VideoShare.repository;

import com.projects.cannon.VideoShare.model.Video;
import com.projects.cannon.VideoShare.service.S3Service;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video,String> {


}
