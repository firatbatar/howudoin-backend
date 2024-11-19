package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.GenericResponse;
import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserRequest;
import edu.sabanciuniv.howudoin.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> addFriend(@RequestBody UserRequest userRequest) {
        if (userRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body(
                    new GenericResponse(GenericResponse.Status.ERROR, "Email cannot be null.", null)
            );
        }

        final HttpStatus httpStatus;
        final GenericResponse response;

        try {
            int status = friendService.sendFriendRequest(userRequest.getEmail());
            switch (status) {
                case -1 -> {
                    httpStatus = HttpStatus.BAD_REQUEST;
                    response = new GenericResponse(
                            GenericResponse.Status.ERROR,
                            "User " + userRequest.getEmail() + " not found.",
                            null
                    );
                }
                case 0 -> {
                    httpStatus = HttpStatus.BAD_REQUEST;
                    response = new GenericResponse(
                            GenericResponse.Status.ERROR,
                            "User " + userRequest.getEmail() + " is already your friend.",
                            null
                    );
                }
                case 1 -> {
                    httpStatus = HttpStatus.OK;
                    response = new GenericResponse(
                            GenericResponse.Status.SUCCESS,
                            "Sent friend request to " + userRequest.getEmail(),
                            null
                    );
                }
                default -> {
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                    response = new GenericResponse(GenericResponse.Status.ERROR, "An error occurred.", null);
                }
            };
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("/accept")
    public ResponseEntity<GenericResponse> acceptFriendRequests() {
        try {
            HashSet<String> friends = friendService.acceptFriendRequests();

            final HttpStatus httpStatus;
            final GenericResponse response;

            if (friends == null) {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                response = new GenericResponse(GenericResponse.Status.ERROR, "An error occurred.", null);
            } else if (friends.isEmpty()) {
                httpStatus = HttpStatus.OK;
                response = new GenericResponse(GenericResponse.Status.SUCCESS, "No friend requests to accept.", null);
            } else {
                httpStatus = HttpStatus.OK;
                response = new GenericResponse(
                        GenericResponse.Status.SUCCESS,
                        "Accepted friend requests from: " + friends,
                        null
                );
            }

            return ResponseEntity.status(httpStatus).body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getFriendList() {
        try {
            HashSet<UserInfoModel> friendList = friendService.getFriendList();
            return ResponseEntity.ok(
                    new GenericResponse(
                            GenericResponse.Status.SUCCESS,
                            null,
                            friendList
                    )
            );
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }
}
