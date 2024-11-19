package edu.sabanciuniv.howudoin.repository;

import edu.sabanciuniv.howudoin.model.GroupModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<GroupModel, String> {
}
