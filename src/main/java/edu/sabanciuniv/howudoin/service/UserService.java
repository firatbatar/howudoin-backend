package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel registerUser(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public UserModel getUser(String email) {
        return userRepository.findById(email).orElse(null);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void updateUser(UserModel userModel) {
        userRepository.save(userModel);
    }
}
