package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        MessageModel message = messageService.sendMessage(messageModel);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<MessageModel>> getMessages() {
        List<MessageModel> messages = messageService.getMessages();
        return ResponseEntity.ok(messages);
    }
}