package edu.sabanciuniv.howudoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document("Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    private String email;
    private String name;
    private String lastname;
    private String password;
    private HashSet<String> friendList = new HashSet<>();
    private HashSet<String> friendRequests = new HashSet<>();
}
