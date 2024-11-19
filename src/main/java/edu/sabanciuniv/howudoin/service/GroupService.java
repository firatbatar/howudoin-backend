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
import java.util.stream.Collectors;

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
        groupModel.getMembers().add(currentUser.getEmail());

        return groupRepository.save(groupModel);
    }

    public void addMember(String groupId, String email) throws Exception {
        try {
            userRepository.findById(email).orElseThrow();
        } catch (NoSuchElementException _) {
            throw new NoSuchElementException("User with email '" + email + "' not found");
        }

        assertMembershipOfCurrentUser(groupId);

        GroupModel groupModel = groupRepository.findById(groupId).orElseThrow();
        groupModel.getMembers().add(email);
    }

    public MessageModel sendMessage(String groupId, MessageModel messageModel) throws Exception {
        assertMembershipOfCurrentUser(groupId);

        UserModel currentUser = getCurrentUser();
        messageModel.setSender(currentUser.getEmail());
        messageModel.setTimestamp(LocalDateTime.now());
        messageModel.setReceiver(groupId);

        return messageRepository.save(messageModel);
    }

    public List<MessageModel> getMessages(String groupId) throws Exception {
        assertMembershipOfCurrentUser(groupId);

        return messageRepository.findByReceiverOrderByTimestampDesc(groupId);
    }

    public HashSet<UserInfoModel> getMembers(String groupId) throws Exception {
        assertMembershipOfCurrentUser(groupId);

        GroupModel groupModel = groupRepository.findById(groupId).orElseThrow();

        return groupModel.getMembers().stream().map(email -> {
            UserModel user = userRepository.findById(email).orElseThrow();
            return new UserInfoModel(user.getEmail(), user.getName(), user.getLastname());
        }).collect(Collectors.toCollection(HashSet::new));
    }

    private UserModel getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(email).orElseThrow();
    }

    private void assertMembershipOfCurrentUser(String groupId) throws Exception {
        UserModel user = getCurrentUser();
        try {
            GroupModel groupModel = groupRepository.findById(groupId).orElseThrow();
            if (!groupModel.getMembers().contains(user.getEmail())) {
                throw new Exception("Current user is not a member of group " + groupId);
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Group with id '" + groupId + "' not found");
        }
    }
}
