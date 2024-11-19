package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.GroupModel;
import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserRequest;
import edu.sabanciuniv.howudoin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public GroupModel createGroup(@RequestBody GroupModel groupModel) {
        return groupService.createGroup(groupModel);
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<String> addMember(
            @PathVariable String groupId,
            @RequestBody UserRequest userRequest
    ) {
        final String email = userRequest.getEmail();
        if (userRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("Email cannot be null.");
        }

        try {
            groupService.addMember(groupId, email);
            return ResponseEntity.ok("Member '" + email + "' added successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/send-message")
    public ResponseEntity<MessageModel> sendMessage(
            @PathVariable String groupId,
            @RequestBody MessageModel messageModel
    ) {
        try {
            MessageModel message = groupService.sendMessage(groupId, messageModel);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            MessageModel message = new MessageModel();
            message.setContent(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/{groupId}/messages")
    public void getMessages(@PathVariable String groupId) {
        // Will be implemented after message service is implemented
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<HashSet<UserInfoModel>> getMembers(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getMembers(groupId));
    }
}
