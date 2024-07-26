package com.TheMFG.E_Commerce.controller;

import com.TheMFG.E_Commerce.model.Cart;
import com.TheMFG.E_Commerce.model.Category;
import com.TheMFG.E_Commerce.model.ProductOrder;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.model.request.OrderRequest;
import com.TheMFG.E_Commerce.service.Interface.CartService;
import com.TheMFG.E_Commerce.service.Interface.CategoryService;
import com.TheMFG.E_Commerce.service.Interface.OrderService;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import com.TheMFG.E_Commerce.util.CommonUtil;
import com.TheMFG.E_Commerce.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;


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
            Integer countCart = cartService.getCountCart(user.getId());
            m.addAttribute("countCart",countCart);
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

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m){
        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts",carts);
        if(carts.size() > 0){
            Double totalOrderPrice = carts.get(carts.size() - 1 ).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/cart";
    }

    private User getLoggedInUserDetails(Principal p){
        String email = p.getName();
        User user = userService.getUserByEmail(email);
        return user;
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid){
        cartService.updateQuantity(sy,cid);
        return "redirect:/user/cart";
    }

    @GetMapping("/orders")
    public String orderPage(Principal p,Model m){
        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts",carts);
        if(carts.size() > 0 ){
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 25 + 50;
            m.addAttribute("orderPrice",orderPrice);
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception{
      //  System.out.println(request);
        User user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(),request);
        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String loadSuccess(){
        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m,Principal p){
        User loginUser = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
        m.addAttribute("orders",orders);
        return "/user/my_orders";
    }

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session){
        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for(OrderStatus orderSt : values){
            if(orderSt.getId().equals(st)){
                status = orderSt.getName()  ;
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id,status);

        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!ObjectUtils.isEmpty(updateOrder)){
            session.setAttribute("succMsg","Sipariş Durumu Güncellendi");
        }else{
            session.setAttribute("errorMsg","Sipariş Durumu Beklenmedik Bir Hatadan Dolayı Güncellenemedi");
        }

        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile(){
        return "/user/profile";
    }
}
