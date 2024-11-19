package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.UserInfoModel;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoModel registerUser(UserModel userModel) {
        UserModel newUser = userRepository.save(userModel);
        return new UserInfoModel(newUser.getEmail(), newUser.getName(), newUser.getLastname());
    }
}
