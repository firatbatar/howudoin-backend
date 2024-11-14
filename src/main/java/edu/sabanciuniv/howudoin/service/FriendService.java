package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.FriendRequest;
import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FriendService {
    private final UserRepository userRepository;

    @Autowired
    public FriendService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int sendFriendRequest(String friendEmail) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findById(email).orElseThrow();
        try {
            UserModel friend = userRepository.findById(friendEmail).orElseThrow();

            if (friend.getFriendList().contains(user.getEmail())) return 0;

            friend.getFriendRequests().add(user.getEmail());
            userRepository.save(friend);
            return 1;
        } catch (NoSuchElementException e) {
            return -1;
        }
    }

    public HashSet<String> acceptFriendRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findById(email).orElseThrow();

        HashSet<String> friendList = user.getFriendList();
        HashSet<String> friendRequests = user.getFriendRequests();
        if (friendRequests.isEmpty()) {
            return null;
        }

        friendRequests.forEach(friendEmail -> {
            UserModel friend = userRepository.findById(friendEmail).orElseThrow();
            friendList.add(friendEmail);
            friend.getFriendList().add(user.getEmail());
            userRepository.save(friend);
        });

        user.setFriendList(friendList);
        user.setFriendRequests(new HashSet<>());
        userRepository.save(user);
        return friendRequests;
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
        } catch (NoSuchElementException e) {
            return friendList;
        }

        return friendList;
    }
}
