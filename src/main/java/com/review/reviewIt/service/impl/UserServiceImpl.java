package com.review.reviewIt.service.impl;


import com.review.reviewIt.entity.Response;
import com.review.reviewIt.dto.UserDTO;
import com.review.reviewIt.entity.User;
import com.review.reviewIt.exception.CustomException;
import com.review.reviewIt.repository.UserRepository;
import com.review.reviewIt.service.iterfac.UserService;
import com.review.reviewIt.utils.JWTUtils;
import com.review.reviewIt.utils.Utils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new CustomException(user.getEmail() + "Already Exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);

        } catch (CustomException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration" + e.getMessage());
        }
        return response;
    }

//    @Override
//    public Response login(LoginRequest loginRequest) {
//        Response response = new Response();
//        try{
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    loginRequest.getEmail() ,loginRequest.getPassword()));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = jwtUtils.generateTokenForUser(authentication);
//            CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
//            Response jwtResponse = new Response(userDetails.getId(), jwt);
//
//            response.setExpirationTime("7 Days");
//            response.setMessage("Successful");
//
//        }catch (CustomException e){
//            response.setStatusCode(400);
//            response.setMessage(e.getMessage());
//
//        }catch (Exception e){
//            response.setStatusCode(500);
//            response.setMessage("Error Occurred During User Registration" + e.getMessage());
//        }
//        return response;
//    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDTOList);

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting all the Users" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId , User authenticatedUser) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new CustomException("User not Found"));

            if (!user.getId().equals(authenticatedUser.getId()) && !authenticatedUser.getRoles().contains("ADMIN")) {
                response.setStatusCode(403); // Forbidden
                response.setMessage("You are not authorized to access this user.");
                return response;
            }
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting the Users By Id" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId, User authenticatedUser) {
        Response response = new Response();

        try {

            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new CustomException("User not Found"));

            if (!user.getId().equals(authenticatedUser.getId()) && !authenticatedUser.getRoles().contains("ADMIN")) {
                response.setStatusCode(403); // Forbidden
                response.setMessage("You are not authorized to access this user.");
                return response;
            }


            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Deleting the User" + e.getMessage());
        }
        return response;
    }

//    @Override
//    public Response updateUser(Long id, User user) {
//        Response response = new Response();
//
//        try {
//            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new CustomException("User not Found"));
//            if(username != null)
//
//            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
//            response.setStatusCode(200);
//            response.setMessage("Successful");
//            response.setUser(userDTO);
//
//        }catch (CustomException e){
//            response.setStatusCode(404);
//            response.setMessage(e.getMessage());
//
//        }catch (Exception e){
//            response.setStatusCode(500);
//            response.setMessage("Error Getting the Users By Id" + e.getMessage());
//        }
//        return response;
//    }

    @Override
    public Response getMyReviews(String userId , User authenticatedUser) {
        Response response = new Response();
        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new CustomException("User Not Found"));

            if (!user.getId().equals(authenticatedUser.getId()) && !authenticatedUser.getRoles().contains("ADMIN")) {
                response.setStatusCode(403); // Forbidden
                response.setMessage("You are not authorized to access this user.");
                return response;
            }


            if (user.getReviews() == null || user.getReviews().isEmpty()) {
                response.setStatusCode(204); // No Content
                response.setMessage("User has no reviews.");
                return response;
            }
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusReviews(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

        }catch (CustomException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting the Review" + e.getMessage());
        }
        return response;
    }


    @Override
    public User getAuthenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Check if authentication is valid
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new CustomException("No authenticated user found");
            }

            String email = authentication.getName();
            User user = userRepository.findByEmail(email);

            // Check if user was found
            if (user == null) {
                throw new CustomException("Authenticated user not found in the system");
            }

            return user;

        } catch (Exception e) {
            throw new CustomException("Error retrieving authenticated user: " + e.getMessage());
        }
    }

}


