package edu.sabanciuniv.howudoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
    @Id
    private String id;
    private String sender;
    private String senderName;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
}
