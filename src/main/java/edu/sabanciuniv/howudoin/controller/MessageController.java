package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.component.Message;
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
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages() {
        return ResponseEntity.ok().build();
    }
}