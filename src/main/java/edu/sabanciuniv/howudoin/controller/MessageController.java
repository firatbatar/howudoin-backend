package edu.sabanciuniv.howudoin.controller;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestBody MessageRequest request,
            @RequestHeader("Authorization") String token) {
        // Assume you have a utility to get user ID from JWT token
        String senderId = JwtUtil.getUserIdFromToken(token);
        Message message = messageService.sendMessage(senderId, request.getReceiverId(), request.getContent());
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(
            @RequestParam String friendId,
            @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdFromToken(token);
        List<Message> messages = messageService.getConversationHistory(userId, friendId);
        return ResponseEntity.ok(messages);
    }
}