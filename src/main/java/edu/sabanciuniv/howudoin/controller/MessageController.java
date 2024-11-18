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
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageModel> sendMessage(@RequestBody MessageModel messageModel) {
        return ResponseEntity.ok(messageModel);
    }

    @GetMapping
    public ResponseEntity<List<MessageModel>> getMessages() {
        return ResponseEntity.ok().build();
    }
}