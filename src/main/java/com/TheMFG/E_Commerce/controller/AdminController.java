package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.Category;
import com.TheMFG.E_Commerce.model.Product;
import com.TheMFG.E_Commerce.model.ProductOrder;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.service.Interface.*;
import com.TheMFG.E_Commerce.util.CommonUtil;
import com.TheMFG.E_Commerce.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void getUserDetails(Principal principal, Model model){
        if(principal != null){
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user",user);
            Integer countCart = cartService.getCountCart(user.getId());
            model.addAttribute("countCart",countCart);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        model.addAttribute("categorys",allActiveCategory);
    }

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model model){
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories",categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model model, @RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name = "pageSize",defaultValue = "5")Integer pageSize){
        Page<Category> page = categoryService.getAllCategoryPagination(pageNo,pageSize);
        List<Category> categorys = page.getContent();
        model.addAttribute("categorys",categorys);
        model.addAttribute("pageNo",page.getNumber());
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("isFirst",page.isFirst());
        model.addAttribute("isLast",page.isLast());

        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file")MultipartFile file, HttpSession session) throws IOException{
        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if(existCategory){
            session.setAttribute("errorMsg","Girilen Kategori Sistemde Mevcut!");
        }else{
            Category saveCategory = categoryService.saveCategory(category);
            if(ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("errorMsg","Kategori Kaydedilirken Beklenmedik Birr Hata Oluştu!");
            }else{
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "kategori" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg","Kategori Başarıyla Kaydedildi!");
            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean deleteCategory = categoryService.deleteCategory(id);
        if(deleteCategory){
            session.setAttribute("succMsg","Kategori Silindi!");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model model){
        model.addAttribute("category",categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category,@RequestParam("file")MultipartFile file,HttpSession session) throws IOException{
        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if(!ObjectUtils.isEmpty(category)){
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if(!ObjectUtils.isEmpty(updateCategory)){
            if(!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "kategori" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Kategori Başarıyla Güncellendi!");
        }else {
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file")MultipartFile image, HttpSession session) throws IOException{
        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product saveProduct = productService.saveProduct(product);

        if(!ObjectUtils.isEmpty(saveProduct)){
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "ürünler" + File.separator + image.getOriginalFilename());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            session.setAttribute("succMsg","Ürün Başarıyla Kaydedildi!");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model model, @RequestParam(defaultValue = "")String ch,
                                  @RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                                  @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
        Page<Product> page = null;
        if(ch != null && ch.length() > 0){
            page = productService.searchProductPagination(pageNo,pageSize, ch);
        }else{
            page = productService.getAllProductsPagination(pageNo,pageSize);
        }
        model.addAttribute("products",page.getContent());
        model.addAttribute("pageNo",page.getNumber());
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("isFirst",page.isFirst());
        model.addAttribute("isLast",page.isLast());

        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteProduct = productService.deleteProduct(id);
        if(deleteProduct){
            session.setAttribute("succMsg","Ürün Silindi!");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id,Model model){
        model.addAttribute("product",productService.getProductById(id));
        model.addAttribute("categories",categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file")MultipartFile image, HttpSession session,Model model){
        if(product.getDiscount() < 0 || product.getDiscount() > 100){
            session.setAttribute("errorMsg","Geçersiz Değer");
        }else{
            Product updateProduct = productService.updateProduct(product,image);
            if(!ObjectUtils.isEmpty(updateProduct)){
                session.setAttribute("succMsg","Ürün Başarıyla Güncellendi!");
            }else {
                session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
            }
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, @RequestParam Integer type){
        List<User> users = null;
        if(type == 1){
            users = userService.getUsers("ROLE_USER");
        }else{
            users = userService.getUsers("ROLE_ADMIN");
        }
        model.addAttribute("userType",type);
        model.addAttribute("users",users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,@RequestParam Integer type, HttpSession session){
        Boolean f = userService.updateAccountStatus(id,status);
        if (f) {
            session.setAttribute("succMsg","Hesap Durumu Güncellendi");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/users?type="+type;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model model,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo, @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
        Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo,pageSize);
        model.addAttribute("orders",page.getContent());
        model.addAttribute("srch",false);
        model.addAttribute("pageNo",page.getNumber());
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("isFirst",page.isFirst());
        model.addAttribute("isLast",page.isLast());
        return "/admin/orders";
    }

    @PostMapping("/update-orders-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session){
        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for(OrderStatus orderSt : values){
            if(orderSt.getId().equals(st)){
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id,status);

        try{
            commonUtil.sendMailForProductOrder(updateOrder,status);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!ObjectUtils.isEmpty(updateOrder)){
            session.setAttribute("succMsg","Durum Güncellendi!");
        }else{
            session.setAttribute("errorMsg", "Durum Güncellenemedi" );
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/search-order")
    public String searchProduct(@RequestParam String orderId, Model model, HttpSession session,
                                @RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10")Integer pageSize){
        if(orderId != null && orderId.length() > 0){
            ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());
            if(ObjectUtils.isEmpty(order)){
                session.setAttribute("errorMsg","Sipariş No Yanlış");
                model.addAttribute("orderDtls",null);
            }else{
                model.addAttribute("orderDtls",order);
            }
            model.addAttribute("srch",true);
        }else{
            List<ProductOrder> allOrders = orderService.getAllOrders();
            model.addAttribute("orders",allOrders);
            model.addAttribute("srch",false);
            Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo,pageSize);
            model.addAttribute("orders",page);
            model.addAttribute("srch",false);
            model.addAttribute("pageNo",page.getNumber());
            model.addAttribute("pageSize",pageSize);
            model.addAttribute("totalElements",page.getTotalElements());
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("isFirst",page.isFirst());
            model.addAttribute("isLast",page.isLast());
        }
        return "/admin/orders";
    }

    @GetMapping("/add-admin")
    public String loadAdminAdd(){
        return "/admin/add_admin";
    }

    @PostMapping("/save-admin")
    public String saveAdmin(@ModelAttribute User user, @RequestParam("img")MultipartFile file, HttpSession session) throws IOException{
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfilImage(imageName);
        User saveUser = userService.saveAdmin(user);

        if(!ObjectUtils.isEmpty(saveUser)){
            if (!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profil" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Admin Kaydı Başarıyla Oluşturuldu");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/add-admin";
    }

    @GetMapping("/profile")
    public String profile(){
        return "/admin/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute User user,@RequestParam MultipartFile img, HttpSession session){
        User updateUserProfile = userService.updateUserProfile(user,img);
        if(ObjectUtils.isEmpty(updateUserProfile)){
            session.setAttribute("errorMsg","Profil Bulunamadı!");
        }else {
            session.setAttribute("succMsg","Profil Güncellendi");
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,@RequestParam String currentPassword, Principal principal, HttpSession session){
        User loggedInUserDetails = commonUtil.getLoggedInUserDetails(principal);
        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());
        if(matches){
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            User updateUser = userService.updateUser(loggedInUserDetails);
            if(ObjectUtils.isEmpty(updateUser)){
                session.setAttribute("errorMsg","Şifre Güncellenemedi! Beklenmedik Bir Hata Oluştu");
            }else {
                session.setAttribute("succMsg","Şifre Güncellendi!");
            }
        }else{
            session.setAttribute("errorMsg","Şifre Doğru Değil");
        }
        return "redirect:/admin/profile";
    }
}
