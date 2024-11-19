package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.component.JwtHelperUtils;
import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FriendService extends GenericService {
    @Autowired
    public FriendService(UserRepository userRepository) {
        super(userRepository);
    }

    public int sendFriendRequest(String friendEmail) {
        UserModel user = this.getCurrentUser();

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
        UserModel user = this.getCurrentUser();

        HashSet<String> friendList = user.getFriendList();
        HashSet<String> friendRequests = user.getFriendRequests();
        try {
            friendRequests.forEach(friendEmail -> {
                UserModel friend = userRepository.findById(friendEmail).orElseThrow();
                friendList.add(friendEmail);
                friend.getFriendList().add(user.getEmail());
                userRepository.save(friend);
            });
        } catch (NoSuchElementException e) {
            return null;
        }

        user.setFriendList(friendList);
        user.setFriendRequests(new HashSet<>());
        userRepository.save(user);
        return friendRequests;
    }

    public List<UserInfoModel> getFriendList() {
        UserModel user = this.getCurrentUser();

        ArrayList<UserInfoModel> friendList = new ArrayList<>();
        user.getFriendList().forEach(friendEmail -> {
            try {
                UserModel friend = userRepository.findById(friendEmail).orElseThrow();
                friendList.add(new UserInfoModel(friend.getEmail(), friend.getName(), friend.getLastname()));
            } catch (NoSuchElementException _) {
                friendList.add(new UserInfoModel(friendEmail, null, null));
            }
        });

        return friendList;
    }
}
