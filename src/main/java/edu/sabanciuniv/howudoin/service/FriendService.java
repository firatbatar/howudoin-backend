package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.FriendRequest;
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

    public Boolean sendFriendRequest(FriendRequest friendRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findById(email).orElseThrow();
        try {
            UserModel friend = userRepository.findById(friendRequest.getEmail()).orElseThrow();
            friend.getFriendRequests().add(user.getEmail());
            userRepository.save(friend);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<String> acceptFriendRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findById(email).orElseThrow();

        List<String> friendList = user.getFriendList();
        List<String> friendRequests = user.getFriendRequests();
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
        user.setFriendRequests(new ArrayList<>());
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
