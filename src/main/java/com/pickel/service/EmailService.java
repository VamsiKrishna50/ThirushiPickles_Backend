//package com.pickel.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import com.pickel.dto.ContactRequest;
//
//@Service
//public class EmailService {
//    
//    @Autowired
//    private JavaMailSender mailSender;
//    
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//    
//    public void sendContactEmail(ContactRequest request) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo("nallavamsi98@gmail.com");
//            message.setSubject("Contact Form: " + request.getSubject());
//            message.setText(
//                "Name: " + request.getName() + "\n" +
//                "Email: " + request.getEmail() + "\n\n" +
//                "Message:\n" + request.getMessage()
//            );
//            
//            mailSender.send(message);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send email: " + e.getMessage());
//        }
//    }
//}

package com.pickel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.pickel.dto.ContactRequest;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;
    
    /**
     * Send contact form email
     */
    public void sendContactEmail(ContactRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("nallavamsi98@gmail.com");
            message.setSubject("Contact Form: " + request.getSubject());
            message.setText(
                "Name: " + request.getName() + "\n" +
                "Email: " + request.getEmail() + "\n\n" +
                "Message:\n" + request.getMessage()
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
    
    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String toEmail, String token, String fullName) {
        try {
            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Thirushi Pickles - Password Reset Request");
            message.setText(
                "Hello " + fullName + ",\n\n" +
                "You have requested to reset your password for your Thirushi Pickles account.\n\n" +
                "Please click the link below to reset your password:\n" +
                resetUrl + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Thirushi Pickles Team"
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
    
    /**
     * Send password reset confirmation email
     */
    public void sendPasswordResetConfirmation(String toEmail, String fullName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Thirushi Pickles - Password Reset Successful");
            message.setText(
                "Hello " + fullName + ",\n\n" +
                "Your password has been successfully reset.\n\n" +
                "If you did not make this change, please contact us immediately.\n\n" +
                "Best regards,\n" +
                "Thirushi Pickles Team"
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't throw - this is a confirmation email
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }
}
