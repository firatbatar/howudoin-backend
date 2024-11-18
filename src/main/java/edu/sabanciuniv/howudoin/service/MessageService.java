package edu.sabanciuniv.howudoin.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(String senderId, String receiverId, String content) {
        Message message = new Message(senderId, receiverId, content);
        return messageRepository.save(message);
    }

    public List<Message> getConversationHistory(String user1Id, String user2Id) {
        List<Message> sentMessages = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampDesc(user1Id, user2Id);
        List<Message> receivedMessages = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampDesc(user2Id, user1Id);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        allMessages.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        return allMessages;
    }
}
