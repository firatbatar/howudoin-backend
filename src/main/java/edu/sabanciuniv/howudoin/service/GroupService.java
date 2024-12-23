package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.GroupModel;
import edu.sabanciuniv.howudoin.model.MessageModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.GroupRepository;
import edu.sabanciuniv.howudoin.repository.MessageRepository;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GroupService extends GenericService {
    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public GroupService(
            GroupRepository groupRepository,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
        super(userRepository);
        this.groupRepository = groupRepository;
        this.messageRepository = messageRepository;
    }

    public GroupModel createGroup(GroupModel groupModel) {
        UserModel currentUser = this.getCurrentUser();

        groupModel.getMembers().add(currentUser.getEmail());

        GroupModel savedGroup = this.groupRepository.save(groupModel);

        savedGroup.getMembers().forEach(email -> {
            try {
                UserModel user = this.getUserByEmail(email);
                user.getGroupList().add(savedGroup.getId());
                this.userRepository.save(user);
            } catch (NoSuchElementException e) {
                System.out.println("User with email '" + email + "' not found");
            }
        });

        return savedGroup;
    }

    public GroupModel addMember(String groupId, String email) throws Exception {
        this.getUserByEmail(email);
        this.assertMembershipOfCurrentUser(groupId);

        GroupModel groupModel = this.groupRepository.findById(groupId).orElseThrow();
        UserModel newMember = this.getUserByEmail(email);

        newMember.getGroupList().add(groupId);
        groupModel.getMembers().add(email);

        this.userRepository.save(newMember);
        return this.groupRepository.save(groupModel);
    }

    public MessageModel sendMessage(String groupId, MessageModel messageModel) throws Exception {
        this.assertMembershipOfCurrentUser(groupId);

        UserModel currentUser = this.getCurrentUser();
        messageModel.setSender(currentUser.getEmail());
        messageModel.setSenderName(currentUser.getName() + " " + currentUser.getLastname());
        messageModel.setTimestamp(LocalDateTime.now());
        messageModel.setReceiver(groupId);

        return this.messageRepository.save(messageModel);
    }

    public List<MessageModel> getMessages(String groupId) throws Exception {
        this.assertMembershipOfCurrentUser(groupId);

        return this.messageRepository.findByReceiverOrderByTimestampDesc(groupId);
    }

    public HashSet<UserModel> getMembers(String groupId) throws Exception {
        this.assertMembershipOfCurrentUser(groupId);

        GroupModel groupModel = this.groupRepository.findById(groupId).orElseThrow();

        return groupModel.getMembers()
                .stream()
                .map(email -> this.getUserByEmail(email).hideInfo()).collect(Collectors.toCollection(HashSet::new));
    }

    public HashSet<GroupModel> getGroupList() {
        UserModel user = this.getCurrentUser();

        return user.getGroupList().stream().map(groupId -> {
            try {
                return this.groupRepository.findById(groupId).orElseThrow();
            } catch (NoSuchElementException _) {
                return new GroupModel(groupId, null, null);
            }
        }).collect(Collectors.toCollection(HashSet::new));
    }

    private void assertMembershipOfCurrentUser(String groupId) throws Exception {
        UserModel user = this.getCurrentUser();
        try {
            GroupModel groupModel = this.groupRepository.findById(groupId).orElseThrow();
            if (!groupModel.getMembers().contains(user.getEmail())) {
                throw new Exception("Current user is not a member of group " + groupId);
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Group with id '" + groupId + "' not found");
        }
    }
}
