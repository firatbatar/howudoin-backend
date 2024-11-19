package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.GroupModel;
import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.GroupRepository;
import edu.sabanciuniv.howudoin.repository.MessageRepository;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public GroupService(
            GroupRepository groupRepository,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public GroupModel createGroup(GroupModel groupModel) {
        UserModel currentUser = getCurrentUser();
        HashSet<String> members = groupModel.getMembers();
        members.add(currentUser.getEmail());
        groupModel.setMembers(members);
        return groupRepository.save(groupModel);
    }

    public void addMember(String groupId, String email) {
        try {
            userRepository.findById(email).orElseThrow();
        } catch (NoSuchElementException _) {
            throw new NoSuchElementException("User with email '" + email + "' not found");
        }

        try {
            GroupModel groupModel = groupRepository.findById(groupId).orElseThrow();
            groupModel.getMembers().add(email);
            groupRepository.save(groupModel);
        } catch (NoSuchElementException _) {
            throw new NoSuchElementException("Group with id '" + groupId + "' not found");
        }
    }

    public MessageModel sendMessage(String groupId, MessageModel messageModel) throws Exception {
        UserModel currentUser = getCurrentUser();
        try {
            GroupModel groupModel = groupRepository.findById(groupId).orElseThrow();
            if (!groupModel.getMembers().contains(currentUser.getEmail())) {
                throw new Exception("You are not a member of this group");
            }
        } catch (NoSuchElementException _) {
            throw new NoSuchElementException("Group with id '" + groupId + "' not found");
        }

        messageModel.setSender(currentUser.getEmail());
        messageModel.setTimestamp(LocalDateTime.now());
        messageModel.setReceiver(groupId);
        System.out.println(messageModel);
        return messageRepository.save(messageModel);
    }

    public HashSet<UserInfoModel> getMembers(String groupId) {
        GroupModel groupModel = groupRepository.findById(groupId).orElseThrow();

        HashSet<UserInfoModel> members = new HashSet<>();
        groupModel.getMembers().forEach(email -> {
            UserModel user = userRepository.findById(email).orElseThrow();
            members.add(new UserInfoModel(user.getEmail(), user.getName(), user.getLastname()));
        });

        return members;
    }

    private UserModel getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(email).orElseThrow();
    }
}
