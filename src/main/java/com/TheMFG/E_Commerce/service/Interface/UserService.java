package com.TheMFG.E_Commerce.service.Interface;

import com.TheMFG.E_Commerce.model.User;

import java.util.List;

public interface UserService {
    public User saveUser(User user);

    public User getUserByEmail(String email);

    public List<User> getUsers(String role);

    Boolean updateAccountStatus(Integer id, Boolean status);

    public void increaseFailedAttempt(User user);

    public void userAccountLock(User user);

    public boolean unlockAccountTimeExpired(User user);

    public void resetAttempt(int userId);
}
