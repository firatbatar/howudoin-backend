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

    public List<MessageModel> getMessages() {
        UserModel currentUser = this.getCurrentUser();

        return messageRepository.findBySenderOrReceiverOrderByTimestampDesc(currentUser.getEmail(), currentUser.getEmail());
    }

    private UserModel getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(email).orElseThrow();
    }
}
