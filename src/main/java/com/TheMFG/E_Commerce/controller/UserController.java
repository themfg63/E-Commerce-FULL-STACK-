package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("img")MultipartFile file, HttpSession session) throws IOException{
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        User saveUser = userService.saveUser(user);

        if(!ObjectUtils.isEmpty(saveUser)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profil" + File.separator + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Kullanıcı Kaydedildi!");
        }else{
            session.setAttribute("errorMsg","Beklenmedik bir hata oluştu!");
        }
        return "redirect:/register";
    }
}
