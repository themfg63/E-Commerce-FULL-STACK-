package com.TheMFG.E_Commerce.config;

import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.repository.UserRepository;
import com.TheMFG.E_Commerce.service.Interface.UserService;
import com.TheMFG.E_Commerce.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandleImpl extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
        String email = request.getParameter("username");
        User user = userRepository.findByEmail(email);

        if(user != null) {
            if (user.getIsEnable()) {
                if (user.getAccountNonLocked()) {
                    if (user.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                        userService.increaseFailedAttempt(user);
                    } else {
                        userService.userAccountLock(user);
                        exception = new LockedException("Hesabınız Bloke Edildi!! Başarısız Deneme Sayısı = 3");
                    }
                } else {
                    if (userService.unlockAccountTimeExpired(user)) {
                        exception = new LockedException("Hesabınız Bloke Edildi!! Lütfen Tekrar Giriş Yapmayı Deneyiniz ");
                    } else {
                        exception = new LockedException("Hesabınız Bloke Edildi!! Lütfen Daha Sonra Tekrar Giriş Yapmayı Deneyiniz");
                    }
                }
            } else {
                exception = new LockedException("Hesabınız Aktifleştirilmedi! ");
            }
        }else{
            exception = new LockedException("Email veya Şifre Hatalı");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request,response,exception);
    }
}
