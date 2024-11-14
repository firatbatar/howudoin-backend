package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FriendService {
    private final UserRepository userRepository;

    @Autowired
    public FriendService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserInfoModel> getFriendList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findById(email).orElseThrow();

        ArrayList<UserInfoModel> friendList = new ArrayList<>();
        try {
            user.getFriendList().forEach(friendEmail -> {
                UserModel friend = userRepository.findById(friendEmail).orElseThrow();
                friendList.add(new UserInfoModel(friend.getEmail(), friend.getName(), friend.getLastname()));
            });
        } catch (NullPointerException | NoSuchElementException e) {
            return friendList;
        }

        return friendList;
    }
}
