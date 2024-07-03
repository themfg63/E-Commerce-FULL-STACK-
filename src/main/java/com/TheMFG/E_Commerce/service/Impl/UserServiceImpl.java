package com.TheMFG.E_Commerce.service.Impl;

import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.repository.UserRepository;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        User saveUser = userRepository.save(user);
        return saveUser;
    }
}
