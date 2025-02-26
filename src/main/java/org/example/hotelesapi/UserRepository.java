package org.example.hotelesapi;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByToken(String token);
    Boolean existsByToken(String token);
    Boolean existsByEmailAndUser(String email, String user);
    User findByEmail(String email);
}
