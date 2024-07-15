package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.Cart;
import com.TheMFG.E_Commerce.model.Category;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.service.Interface.CartService;
import com.TheMFG.E_Commerce.service.Interface.CategoryService;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

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

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session){
        Cart saveCart = cartService.saveCart(pid,uid);

        if(ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("errorMsg","Ürün Sepetinize Eklenemedi");
        }else{
            session.setAttribute("succMsg","Ürün Sepetinize Eklendi");
        }
        return "redirect:/product/" + pid;
    }
}
