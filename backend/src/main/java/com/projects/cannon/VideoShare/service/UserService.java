package com.projects.cannon.VideoShare.service;

import com.projects.cannon.VideoShare.model.User;
import com.projects.cannon.VideoShare.model.Video;
import com.projects.cannon.VideoShare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser(){

        String sub= ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");

        return userRepository.findBySub(sub)
                .orElseThrow(()-> new IllegalArgumentException("Cannot find user with sub :"+sub));

    }
    private User getUserById(String userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("Cannot find user with userId"+ userId));
        return user;
    }

    public void addToLikedVideos(String videoId) {
        User currentUser=getCurrentUser();
        currentUser.addToLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public boolean ifLikedVideo(String videoId){
        return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo->likedVideo.equals(videoId));
    }
    public boolean ifdisLikedVideo(String videoId){
        return getCurrentUser().getDislikedVideos().stream().anyMatch(likedVideo->likedVideo.equals(videoId));
    }

    public void removeFromLikedVIdeos(String videoId) {
        User currentUser=getCurrentUser();
        currentUser.removeFromLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void removeFromDislikedVideos(String videoId) {
        User currentUser=getCurrentUser();
        currentUser.removeFromDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addToDislikedVideos(String videoId) {
        User currentUser=getCurrentUser();
        currentUser.addToDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addToVideoHistory(String videoId) {
            User currentUser= getCurrentUser();
            currentUser.addToVideoHistory(videoId);
            userRepository.save(currentUser);
    }

    public void subscribeUser(String userId) {
        User currentUser=getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);

        User user = getUserById(userId);

        user.addToSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public void unSubscribeUser(String userId) {
        User currentUser=getCurrentUser();
        currentUser.removeFromSubscribedToUsers(userId);

        User user = getUserById(userId);

        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public Set<String> userHistory(String userId) {

        User user = getUserById(userId);
        return user.getVideoHistory();
    }

    public void subscribe2User(String videoId) {
    }

    private User getChannel(String videoId){
        return null;
    }
}
