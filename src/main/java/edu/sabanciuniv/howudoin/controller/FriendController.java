package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.FriendRequest;
import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestBody FriendRequest friendRequest) {
        Boolean success = friendService.sendFriendRequest(friendRequest);
        if (!success) {
            return ResponseEntity.badRequest().body("User " + friendRequest.getEmail() + " not found.");
        }
        return ResponseEntity.ok("Sent friend request to " + friendRequest.getEmail());
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequests() {
        HashSet<String> friends = friendService.acceptFriendRequests();
        String message = "Accepted friend requests from: " + friends;
        if (friends == null) {
            message = "No friend requests to accept.";
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserInfoModel>> getFriendList() {
        List<UserInfoModel> friendList = friendService.getFriendList();
        return ResponseEntity.ok(friendList);
    }
}
