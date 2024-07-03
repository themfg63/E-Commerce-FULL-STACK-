package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

}
