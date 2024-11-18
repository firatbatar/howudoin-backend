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
    public void addMember(@PathVariable Integer groupId) {
    }

    @PostMapping("/{groupId}/send-message")
    public void sendMessage(@PathVariable Integer groupId) {
    }

    @GetMapping("/{groupId}/messages")
    public void getMessages(@PathVariable Integer groupId) {
    }

    @GetMapping("/{groupId}/members")
    public void getMembers(@PathVariable Integer groupId) {
    }
}
