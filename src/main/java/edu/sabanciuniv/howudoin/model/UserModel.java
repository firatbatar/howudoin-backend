package edu.sabanciuniv.howudoin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel {
    @Id
    private String email;
    private String name;
    private String lastname;
    private String password;
    private HashSet<String> friendList = new HashSet<>();
    private HashSet<String> friendRequests = new HashSet<>();
    private HashSet<String> groupList = new HashSet<>();

    public UserModel hideInfo() {
        return new UserModel(email, name, lastname, null, null, null, null);
    }
}
