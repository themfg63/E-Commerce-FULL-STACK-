package com.TheMFG.E_Commerce.util;

import com.TheMFG.E_Commerce.model.ProductOrder;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;

    public Boolean sendMail(String url,String reciepentEmail) throws UnsupportedEncodingException, MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("themfg.63@gmail.com","Shooping Cart");
        helper.setTo(reciepentEmail);

        String content = "<p>Merhaba,</p>" +
                "<p>Şifre sıfırlama talebinde bulundunuz.</p>" +
                "<p>Şifrenizi sıfırlamak için linke tıklayınız </p>" +
                "<p><a href=\"" + url + "\">Şifremi Değiştir</a></p>" ;
        helper.setSubject("Şifre Güncelleme");
        helper.setText(content,true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg = null;

    public Boolean sendMailForProductOrder(ProductOrder order,String status) throws Exception {

        msg = "<p>Sayın [[name]], </p> <br> <p>Bizi Tercih Ettiğiniz İçin Teşekkür Ederiz. Siparişinizin Durumu:<b> [[orderStatus]] </b> </p>"
                + "<p> <b> Sipariş Detayı: </b> </p>"
                + "<p> Ürün Adı: [[productName]] </p>"
                + "<p> Kategori: [[category]] </p>"
                + "<p> Adet: [[quantity]] </p>"
                + "<p> Fiyat: [[price]] </p>"
                + "<p> Ödeme Türü: [[paymentType]] </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("themfg.63@gmail.com","Shopping Cart");
        helper.setTo(order.getOrderAddress().getEmail());

        msg = msg.replace("[[name]]",order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]",status);
        msg = msg.replace("[[productName",order.getProduct().getTitle());
        msg = msg.replace("[[category]]", order.getProduct().getCategory());
        msg = msg.replace("[[quantity]]", order.getQuantity().toString());
        msg = msg.replace("[[price]]",order.getPrice().toString());
        msg = msg.replace("[[paymentType]]",order.getPaymentType());

        helper.setSubject("Sipariş Durumu: ");
        helper.setText(msg,true);;
        mailSender.send(message);
        return true;
    }

    public User getLoggedInUserDetails(Principal principal){
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        return user;
    }
}
