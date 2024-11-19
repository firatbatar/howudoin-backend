package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserRequest;
import edu.sabanciuniv.howudoin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageModel> sendMessage(@RequestBody MessageModel messageModel) {
        try {
            MessageModel message = messageService.sendMessage(messageModel);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            MessageModel message = new MessageModel();
            message.setContent(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }

    }

    @GetMapping
    public ResponseEntity<List<MessageModel>> getMessages(@RequestBody UserRequest userRequest) {
        List<MessageModel> messages = messageService.getMessages(userRequest.getEmail());
        return ResponseEntity.ok(messages);
    }
}