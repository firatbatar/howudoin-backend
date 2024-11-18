package edu.sabanciuniv.howudoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document("Groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupModel {
    @Id
    private String name;
    private HashSet<String> members;
}
