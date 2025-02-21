package org.example.hotelesapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    public Boolean requestValidation(String token) {
        return userRepository.existsByToken(token);
    }
}
