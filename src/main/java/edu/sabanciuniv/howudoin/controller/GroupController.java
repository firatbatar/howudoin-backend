package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.GenericResponse;
import edu.sabanciuniv.howudoin.model.GroupModel;
import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserModel;
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
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createGroup(@RequestBody GroupModel groupModel) {
        try {
            GroupModel group = this.groupService.createGroup(groupModel);
            return ResponseEntity.ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, group));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<GenericResponse> addMember(
            @PathVariable String groupId,
            @RequestBody UserRequest userRequest
    ) {
        final String email = userRequest.getEmail();
        if (userRequest.getEmail() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, "Email cannot be null.", null));
        }

        try {
            GroupModel updatedGroup = this.groupService.addMember(groupId, email);
            return ResponseEntity
                    .ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, updatedGroup));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @PostMapping("/{groupId}/send-message")
    public ResponseEntity<GenericResponse> sendMessage(
            @PathVariable String groupId,
            @RequestBody MessageModel messageModel
    ) {
        try {
            MessageModel message = this.groupService.sendMessage(groupId, messageModel);
            return ResponseEntity
                    .ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, message));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<GenericResponse> getMessages(@PathVariable String groupId) {
        try {
            List<MessageModel> messages = this.groupService.getMessages(groupId);
            return ResponseEntity
                    .ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, messages));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<GenericResponse> getMembers(@PathVariable String groupId) {
        try {
            HashSet<UserModel> members = groupService.getMembers(groupId);
            return ResponseEntity
                    .ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, members));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getGroupList() {
        try {
            HashSet<GroupModel> groupList = groupService.getGroupList();
            return ResponseEntity.ok(
                    new GenericResponse(
                            GenericResponse.Status.SUCCESS,
                            null,
                            groupList
                    )
            );
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }
}
