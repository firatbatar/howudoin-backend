package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends GenericService {
    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public UserModel registerUser(UserModel userModel) {
        try {
            this.getUserByEmail(userModel.getEmail());
            return null;
        } catch (Exception e) {
            // User not found, continue
        }

        UserModel newUser = this.userRepository.save(userModel);
        return newUser.hideInfo();
    }
}
