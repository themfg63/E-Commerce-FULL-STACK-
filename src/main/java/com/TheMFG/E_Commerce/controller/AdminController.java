package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.Category;
import com.TheMFG.E_Commerce.model.Product;
import com.TheMFG.E_Commerce.service.Interface.CategoryService;
import com.TheMFG.E_Commerce.service.Interface.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

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
    public String category(Model m){
        m.addAttribute("categorys",categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file")MultipartFile file, HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if(existCategory){
            session.setAttribute("errorMsg","Kategori zaten mevcut");
        }else{
            Category saveCategory = categoryService.saveCategory(category);

            if(ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("errorMsg","Kategori kaydedilirken bir hata oluştu!");
            }else{
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "kategori" + File.separator + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);


                session.setAttribute("succMsg","Kategori Kaydedildi!");
            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean deleteCategory = categoryService.deleteCategory(id);

        if(deleteCategory){
            session.setAttribute("succMsg","Kategori Silindi");
        }else{
            session.setAttribute("errorMsg","Kategori silinirken bir hata oluştu");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model m){
        m.addAttribute("category",categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException{
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
                Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Kategori Güncellendi");
        }else{
            session.setAttribute("errorMsg","Beklenmedik bir hata oluştu");
        }

        return "redirect:/admin/loadEditCategory/"+category.getId() ;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,HttpSession session) throws IOException{
        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);

        Product saveProduct = productService.saveProduct(product);

        if(!ObjectUtils.isEmpty(saveProduct)){
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "ürünler" + File.separator + image.getOriginalFilename());
            System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg","Ürün Kaydedildi");
        }else{
            session.setAttribute("errorMsg","Beklenmedik bir hata oluştu");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model model){
        model.addAttribute("products",productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteProduct = productService.deleteProduct(id);
        if(deleteProduct){
            session.setAttribute("succMsg","Ürün Silindi");
        }else{
            session.setAttribute("errorMsg","Beklenmedik Bir Hata Oluştu!");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model model){
        model.addAttribute("product",productService.getProductById(id));
        model.addAttribute("categories",categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image, HttpSession session,Model m){
        Product updateProduct = productService.updateProduct(product,image);
        if(!ObjectUtils.isEmpty(updateProduct)){
            session.setAttribute("succMsg","Ürün Güncellendi");
        }else{
            session.setAttribute("errorMsg","Bir Hata Oluştu!");
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }
}
