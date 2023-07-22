package com.projects.cannon.VideoShare.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.cannon.VideoShare.dto.UserInfoDto;
import com.projects.cannon.VideoShare.model.User;
import com.projects.cannon.VideoShare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public String registerUser(String tokenValue){

        HttpRequest httpRequest= HttpRequest.newBuilder()
                                            .GET()
                                            .uri(URI.create(userInfoEndpoint))
                                            .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                                            .build();

        HttpClient httpClient=   HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {
            HttpResponse<String> responseString= httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body=responseString.body();

            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            UserInfoDto userInfoDto= objectMapper.readValue(body, UserInfoDto.class);

            Optional<User> userBySubject = userRepository.findBySub(userInfoDto.getSub());
            if(userBySubject.isPresent()){
                String id= userBySubject.get().getId();
                return id;
            }else {
                User user = new User();
                user.setFirstName(userInfoDto.getGivenName());
                user.setLastName(userInfoDto.getLastName());
                user.setFullName(userInfoDto.getName());
                user.setEmailAddress(userInfoDto.getEmail());
                user.setSub(userInfoDto.getSub());

               User repoUser= userRepository.save(user);
               return repoUser.getId();
            }

        } catch(Exception exception) {
            throw new RuntimeException("Exception occured while registering user",exception);
        }
    }

}

