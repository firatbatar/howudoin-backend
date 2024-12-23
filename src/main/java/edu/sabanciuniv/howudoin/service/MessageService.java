package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.MessageRepository;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService extends GenericService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        super(userRepository);
        this.messageRepository = messageRepository;
    }

    public MessageModel sendMessage(MessageModel messageModel) throws Exception {
        UserModel currentUser = this.getCurrentUser();

        if (!currentUser.getFriendList().contains(messageModel.getReceiver())) {
            throw new Exception(messageModel.getReceiver() + " is not your friend.");
        }

        messageModel.setSender(currentUser.getEmail());
        messageModel.setSenderName(currentUser.getName() + " " + currentUser.getLastname());
        messageModel.setTimestamp(LocalDateTime.now());

        return messageRepository.save(messageModel);
    }

    public List<MessageModel> getMessages(String friendEmail) {
        UserModel currentUser = this.getCurrentUser();

        List<MessageModel> sentMessages =
                messageRepository.findBySenderAndReceiverOrderByTimestampDesc(currentUser.getEmail(), friendEmail);
        List<MessageModel> receivedMessages =
                messageRepository.findBySenderAndReceiverOrderByTimestampDesc(friendEmail, currentUser.getEmail());

        List<MessageModel> messages = new ArrayList<>();
        messages.addAll(sentMessages);
        messages.addAll(receivedMessages);
        messages.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));

        return messages;
    }
}
