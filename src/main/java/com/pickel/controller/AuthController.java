//package com.pickel.controller;
//
//import com.pickel.dto.JwtResponse;
//import com.pickel.dto.LoginRequest;
//import com.pickel.dto.RegisterRequest;
//import com.pickel.service.AuthService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
//public class AuthController {
//    
//    @Autowired
//    private AuthService authService;
//    
//    @PostMapping("/register")
//    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest request) {
//        JwtResponse response = authService.register(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//    
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
//        JwtResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }
//    
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout() {
//        return ResponseEntity.ok("Logged out successfully");
//    }
//}

package com.pickel.controller;

import com.pickel.dto.ForgotPasswordRequest;
import com.pickel.dto.JwtResponse;
import com.pickel.dto.LoginRequest;
import com.pickel.dto.RegisterRequest;
import com.pickel.service.AuthService;
import com.pickel.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest request) {
        JwtResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    
    /**
     * Forgot password - send reset email
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.createPasswordResetToken(request);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password reset link sent to your email"
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "If an account exists with this email, a password reset link will be sent"
            ));
        }
    }
    
    /**
     * Validate reset token
     */
    @GetMapping("/reset-password/validate")
    public ResponseEntity<Map<String, Object>> validateResetToken(@RequestParam String token) {
        boolean isValid = passwordResetService.validateToken(token);
        return ResponseEntity.ok(Map.of(
            "valid", isValid,
            "message", isValid ? "Token is valid" : "Invalid or expired token"
        ));
    }
    
    /**
     * Reset password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String newPassword = payload.get("newPassword");
            
            if (token == null || newPassword == null || newPassword.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid request. Password must be at least 6 characters"
                ));
            }
            
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password reset successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}