package com.review.reviewIt.service.iterfac;

import com.review.reviewIt.dto.LoginRequest;
import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    Response register(User user);
//
//    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserById(String userId , User authenticatedUser );

    Response deleteUser(String userId ,User authenticatedUser );

//    Response updateUser(Long userid ,String user);

    Response getMyReviews(String userId ,User authenticatedUser);

    User getAuthenticated();

}
