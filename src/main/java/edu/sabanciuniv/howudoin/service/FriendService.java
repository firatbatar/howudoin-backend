package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FriendService extends GenericService {
    @Autowired
    public FriendService(UserRepository userRepository) {
        super(userRepository);
    }

    public int sendFriendRequest(String friendEmail) {
        UserModel user = this.getCurrentUser();

        try {
            UserModel friend = this.getUserByEmail(friendEmail);

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
                UserModel friend = this.getUserByEmail(friendEmail);
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

    public HashSet<UserModel> getFriendList() {
        UserModel user = this.getCurrentUser();

        return user.getFriendList().stream().map(friendEmail -> {
            try {
                return this.getUserByEmail(friendEmail).hideInfo();
            } catch (NoSuchElementException _) {
                return new UserModel(friendEmail, null, null, null, null, null, null);
            }
        }).collect(Collectors.toCollection(HashSet::new));
    }
}
