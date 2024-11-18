package edu.sabanciuniv.howudoin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @PostMapping("/create")
    public void createGroup() {
    }

    @PostMapping("/{groupId}/add-member")
    public void addMember(@PathVariable int groupId) {
    }

    @PostMapping("/{groupId}/send-message")
    public void sendMessage(@PathVariable int groupId) {
    }

    @GetMapping("/{groupId}/messages")
    public void getMessages(@PathVariable int groupId) {
    }

    @GetMapping("/{groupId}/members")
    public void getMembers(@PathVariable int groupId) {
    }
}
