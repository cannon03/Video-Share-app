package com.projects.cannon.VideoShare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String id;
    @JsonProperty("sub")
    private String sub;
    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("name")
    private String name;
    @JsonProperty("picture")
    private String picture;
    private String email;
}
