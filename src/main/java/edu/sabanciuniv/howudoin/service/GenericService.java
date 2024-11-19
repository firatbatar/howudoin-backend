package edu.sabanciuniv.howudoin.service;

import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;

public abstract class GenericService {
    protected final UserRepository userRepository;

    protected GenericService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected UserModel getCurrentUser() throws NoSuchElementException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findById(email).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("User not found");
        }
    }

    protected UserModel getUserByEmail(String email) throws NoSuchElementException {
        try {
            return userRepository.findById(email).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("User with email '" + email + "' not found");
        }
    }
}
