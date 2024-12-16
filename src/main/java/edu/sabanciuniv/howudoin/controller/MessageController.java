package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.model.GenericResponse;
import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<GenericResponse> sendMessage(@RequestBody MessageModel messageModel) {
        try {
            MessageModel message = messageService.sendMessage(messageModel);
            return ResponseEntity
                    .ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, message));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getMessages(@RequestParam("history") String id) {
        try {
            List<MessageModel> messages = messageService.getMessages(id);
            return ResponseEntity
                    .ok(new GenericResponse(GenericResponse.Status.SUCCESS, null, messages));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new GenericResponse(GenericResponse.Status.ERROR, e.getMessage(), null));
        }
    }
}