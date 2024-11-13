package com.review.reviewIt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    private List<String> roles;

    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

}
