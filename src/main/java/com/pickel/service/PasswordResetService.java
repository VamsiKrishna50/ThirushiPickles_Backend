package com.pickel.service;

import com.pickel.dto.ForgotPasswordRequest;
import com.pickel.entity.PasswordResetToken;
import com.pickel.entity.User;
import com.pickel.exception.ResourceNotFoundException;
import com.pickel.repository.PasswordResetTokenRepository;
import com.pickel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final int EXPIRATION_HOURS = 24;
    
    /**
     * Generate and send password reset token
     */
    @Transactional
    public void createPasswordResetToken(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        // Delete any existing tokens for this user
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
        
        // Generate new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        
        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        tokenRepository.save(resetToken);
        
        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), token, user.getFullName());
    }
    
    /**
     * Validate reset token
     */
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElse(null);
        
        if (resetToken == null) {
            return false;
        }
        
        return !resetToken.isExpired() && !resetToken.getUsed();
    }
    
    /**
     * Reset password using token
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));
        
        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }
        
        if (resetToken.getUsed()) {
            throw new RuntimeException("Token has already been used");
        }
        
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
    
    /**
     * Clean up expired tokens (can be scheduled)
     */
    @Transactional
    public void deleteExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}