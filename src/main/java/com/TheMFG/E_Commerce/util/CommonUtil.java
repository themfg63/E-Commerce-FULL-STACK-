package com.TheMFG.E_Commerce.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

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

    public static String generateUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
