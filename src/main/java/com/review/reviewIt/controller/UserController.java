package com.review.reviewIt.controller;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.service.iterfac.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-id/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {

        User authenticatedUser = userService.getAuthenticated();

        Response response = userService.getUserById(userId , authenticatedUser);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {

        User authenticatedUser = userService.getAuthenticated();

        Response response = userService.deleteUser(userId ,authenticatedUser );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/get-user-reviews/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Response> getUserReviews(@PathVariable("userId") String userId){

        User authenticatedUser = userService.getAuthenticated();

        Response response = userService.getMyReviews(userId , authenticatedUser);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
