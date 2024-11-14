package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void addFriend() {
    }

    @PostMapping("/accept")
    public void acceptFriendRequest() {
    }

    @GetMapping("/")
    public ResponseEntity<List<UserInfoModel>> getFriendList() {
        List<UserInfoModel> friendList = friendService.getFriendList();
        return ResponseEntity.ok(friendList);
    }
}
