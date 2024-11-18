package edu.sabanciuniv.howudoin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInfoModel {
    private String email;
    private String name;
    private String lastname;
}
