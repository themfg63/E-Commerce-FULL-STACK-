package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.Category;
import com.TheMFG.E_Commerce.model.Product;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.service.Interface.CartService;
import com.TheMFG.E_Commerce.service.Interface.CategoryService;
import com.TheMFG.E_Commerce.service.Interface.ProductService;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import com.TheMFG.E_Commerce.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if (p != null){
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            m.addAttribute("user",user);
            Integer countCart = cartService.getCountCart(user.getId());
            m.addAttribute("countCart",countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("category",allActiveCategory);
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/products")
    public String products(Model model,@RequestParam(value = "category",defaultValue = "") String category,
                           @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,
                           @RequestParam(name = "pageSize",defaultValue = "2") Integer pageSize ){
        List<Category> categories = categoryService.getAllActiveCategory();
        model.addAttribute("paramValue",category);
        model.addAttribute("categories",categories);

        Page<Product> page = productService.getAllActiveProductPagination(pageNo,pageSize,category);
        List<Product> products = page.getContent();
        model.addAttribute("products",products);
        model.addAttribute("productsSize",products.size());
        model.addAttribute("pageNo",page.getNumber());
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("isFirst",page.isFirst());
        model.addAttribute("isLast",page.isLast());

        return "product";
    }
    @GetMapping("/product/{id}")
    public String product(@PathVariable int id,Model model){
        Product productById = productService.getProductById(id);
        model.addAttribute("product",productById);
        return "view_product";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("img")MultipartFile file, HttpSession session) throws IOException{
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfilImage(imageName);
        User saveUser = userService.saveUser(user);

        if(!ObjectUtils.isEmpty(saveUser)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profil" + File.separator + file.getOriginalFilename());
                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Kullanıcı Oluşturuldu!");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir hata oluştu");
        }
        return "redirect:/register";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword(){
        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        User userByEmail = userService.getUserByEmail(email);

        if(ObjectUtils.isEmpty(userByEmail)){
            session.setAttribute("errorMsg","Geçersiz Mail Adresi");
        }else {
            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email,resetToken);

            String url = CommonUtil.generateUrl(request ) + "/reset-password?token=" + resetToken;

            Boolean sendMail = commonUtil.sendMail(url,email );

            if(sendMail){
                session.setAttribute("succMsg","Lütfen mail adresinizi kontrol ediniz.. Şifre güncelleme linki gönderildi.");
            }else{
                session.setAttribute("errorMsg","Beklenmedik bir hata oluştu! Şifre güncelleme linki gönderilemedi.");
            }
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String token, HttpSession session, Model m){
        User userByToken = userService.getUserByToken(token);

        if(userByToken == null){
            m.addAttribute("msg","Şifre güncelleme linkiniz geçersiz veya süresi dolmuş! ");
            return "message";
        }
        m.addAttribute("token",token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password,HttpSession session,Model model){
        User userByToken = userService.getUserByToken(token);
        if(userByToken == null){
            model.addAttribute("errorMsg","Şifre güncelleme linkiniz geçersiz veya süresi dolmuş !");
            return "message";
        }else{
            userByToken.setPassword(passwordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);
          //  session.setAttribute("succMsg","Şifreniz başarıyla güncellendi");
            model.addAttribute("msg","Şifreniz başarıyla güncellendi");
            return "message";
        }
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model model){
        List<Product> searchProducts = productService.searchProduct(ch);
        model.addAttribute("products",searchProducts);
        List<Category> categories = categoryService.getAllActiveCategory();
        model.addAttribute("categories",categories);
        return "product";
    }
}


