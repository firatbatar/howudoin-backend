package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.MessageRepository;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public MessageModel sendMessage(MessageModel messageModel) {
        UserModel currentUser = this.getCurrentUser();

        messageModel.setSender(currentUser.getEmail());
        messageModel.setTimestamp(LocalDateTime.now());

        return messageRepository.save(messageModel);
    }

    public List<MessageModel> getConversationHistory(String user1Id, String user2Id) {
        List<MessageModel> sentMessageModels = messageRepository.findBySenderAndReceiverOrderByTimestampDesc(user1Id, user2Id);
        List<MessageModel> receivedMessageModels = messageRepository.findBySenderAndReceiverOrderByTimestampDesc(user2Id, user1Id);

        List<MessageModel> allMessageModels = new ArrayList<>();
        allMessageModels.addAll(sentMessageModels);
        allMessageModels.addAll(receivedMessageModels);

        allMessageModels.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        return allMessageModels;
    }

    private UserModel getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(email).orElseThrow();
    }
}
