package edu.sabanciuniv.howudoin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderIdAndReceiverIdOrderByTimestampDesc(String senderId, String receiverId);
    List<Message> findByGroupIdOrderByTimestampDesc(String groupId);
}
