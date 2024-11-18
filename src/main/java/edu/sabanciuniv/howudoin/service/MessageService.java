package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageModel sendMessage(MessageModel messageModel) {
        return messageRepository.save(messageModel);
    }

    public List<MessageModel> getConversationHistory(String user1Id, String user2Id) {
        List<MessageModel> sentMessageModels = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampDesc(user1Id, user2Id);
        List<MessageModel> receivedMessageModels = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampDesc(user2Id, user1Id);

        List<MessageModel> allMessageModels = new ArrayList<>();
        allMessageModels.addAll(sentMessageModels);
        allMessageModels.addAll(receivedMessageModels);

        allMessageModels.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        return allMessageModels;
    }
}
