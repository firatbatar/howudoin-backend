package edu.sabanciuniv.howudoin.repository;

import edu.sabanciuniv.howudoin.model.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<MessageModel, String> {
    List<MessageModel> findBySenderAndReceiverOrderByTimestampDesc(String sender, String receiver);
}
