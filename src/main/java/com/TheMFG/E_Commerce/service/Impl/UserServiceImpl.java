package com.TheMFG.E_Commerce.service.Impl;

import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.repository.UserRepository;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user){
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        User saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role){
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Integer id,Boolean status){
        Optional<User> findByUser = userRepository.findById(id);

        if(findByUser.isPresent()){
            User user = findByUser.get();
            user.setIsEnable(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
