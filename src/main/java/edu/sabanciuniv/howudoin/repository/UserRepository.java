package edu.sabanciuniv.howudoin.repository;

import edu.sabanciuniv.howudoin.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel, String> {
}
