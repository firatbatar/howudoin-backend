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
        if (friendRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("Email cannot be null.");
        }
        int status = friendService.sendFriendRequest(friendRequest.getEmail());

        return switch (status) {
            case -1 -> ResponseEntity.badRequest().body("User " + friendRequest.getEmail() + " not found.");
            case 0 -> ResponseEntity.badRequest().body("User " + friendRequest.getEmail() + " is already your friend.");
            case 1 -> ResponseEntity.ok("Sent friend request to " + friendRequest.getEmail());
            default -> ResponseEntity.badRequest().body("An error occurred.");
        };
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequests() {
        HashSet<String> friends = friendService.acceptFriendRequests();

        if (friends == null) {
            return ResponseEntity.badRequest().body("An error occurred.");
        } else if (friends.isEmpty()) {
            return ResponseEntity.badRequest().body("No friend requests to accept.");
        } else {
            return ResponseEntity.ok("Accepted friend requests from: " + friends);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<UserInfoModel>> getFriendList() {
        List<UserInfoModel> friendList = friendService.getFriendList();
        return ResponseEntity.ok(friendList);
    }
}
