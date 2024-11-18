package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.GroupModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.GroupRepository;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(
            GroupRepository groupRepository,
            UserRepository userRepository
    ) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public GroupModel createGroup(GroupModel groupModel) {
        UserModel currentUser = getCurrentUser();
        HashSet<String> members = groupModel.getMembers();
        members.add(currentUser.getEmail());
        groupModel.setMembers(members);
        return groupRepository.save(groupModel);
    }

    private UserModel getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(email).orElseThrow();
    }
}
