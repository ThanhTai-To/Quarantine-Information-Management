package com.thanhtai.quarantineinformationmanagement.repository;

import com.thanhtai.quarantineinformationmanagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
