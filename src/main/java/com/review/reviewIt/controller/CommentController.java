package com.review.reviewIt.controller;

import com.review.reviewIt.entity.Response;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.service.iterfac.CommentService;
import com.review.reviewIt.service.iterfac.UserService;
import com.review.reviewIt.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> addComment( @RequestParam(value = "reviewId" , required = false) Long reviewId,
                                                @RequestParam(value = "text" , required = false) String text
                                                ){
        if(reviewId == null || text == null || text.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please Provide for all fields(ReviewId ,Comment Text)");

            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        //User Authentication Logic
        User authenticatedUser = userService.getAuthenticated();

        Response response = commentService.addComment(reviewId,text ,authenticatedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/update-comment/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> updateComment(@PathVariable Long commentId,
                                                 @RequestParam(value = "text" , required = false) String text){

        // Check if the text is provided
        if (text == null || text.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400); // Bad Request
            response.setMessage("Comment text cannot be empty.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        User authenticatedUser = userService.getAuthenticated();

        Response response = commentService.updateComment(commentId , text ,authenticatedUser);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> deleteComment(@PathVariable Long commentId){
        User authenticatedUser = userService.getAuthenticated();

        Response response = commentService.deleteComment(commentId , authenticatedUser);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/get-comment-by-id/{commentId}")
    public ResponseEntity<Response> getCommentById(@PathVariable Long commentId){
        Response response = commentService.getCommentById(commentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("get-comment-by-reviews/{reviewId}")
    public ResponseEntity<Response> getCommentByReviewId(@PathVariable Long reviewId){
        Response response = commentService.getCommentsByReview(reviewId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("get-comment-by-users/{userId}")
    public ResponseEntity<Response> getCommentByUserId(@PathVariable Long userId){
        Response response = commentService.getCommentsByUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
