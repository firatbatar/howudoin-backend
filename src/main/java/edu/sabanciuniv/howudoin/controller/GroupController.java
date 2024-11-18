package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.GroupModel;
import edu.sabanciuniv.howudoin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
