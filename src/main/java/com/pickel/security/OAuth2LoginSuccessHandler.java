package com.pickel.security;

import com.pickel.entity.User;
import com.pickel.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");
        
        User user = userRepository.findByEmail(email).orElse(null);
        
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRole(User.Role.CUSTOMER);
            user.setOauthProvider("google");
            user.setOauthProviderId(providerId);
            user.setEnabled(true);
            userRepository.save(user);
        }
        
        String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());
        
        String redirectUrl = String.format("http://localhost:3000/oauth2/redirect?token=%s", jwt);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}