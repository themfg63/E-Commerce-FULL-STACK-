package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.Category;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.service.Interface.CategoryService;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if (p != null){
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            m.addAttribute("user",user);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("category",allActiveCategory);
    }
}
