package com.review.reviewIt.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rating;
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-reviews")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference("restaurant-reviews")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review" , cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("review-comments")
    private List<Comment> comments;

    //Method to get the user ID
    public Long getUserId(){
        return user !=null ? user.getId(): null;
    }


}
