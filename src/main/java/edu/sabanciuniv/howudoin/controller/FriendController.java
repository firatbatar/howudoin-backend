package edu.sabanciuniv.howudoin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {
    @PostMapping("/add")
    public void addFriend() {
    }

    @PostMapping("/accept")
    public void acceptFriendRequest() {
    }

    @GetMapping("/")
    public void getFriendList() {
    }
}
