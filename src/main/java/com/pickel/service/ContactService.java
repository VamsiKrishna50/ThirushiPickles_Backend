package com.pickel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pickel.dto.ContactRequest;
import com.pickel.entity.Contact;
import com.pickel.entity.ContactStatus;
import com.pickel.repository.ContactRepository;
import java.util.List;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired(required = false)
    private EmailService emailService;
    
    public Contact processContactRequest(ContactRequest request) {
        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setSubject(request.getSubject());
        contact.setMessage(request.getMessage());
        contact.setStatus(ContactStatus.NEW);
        
        Contact savedContact = contactRepository.save(contact);
        
        // Send email if EmailService is available
        if (emailService != null) {
            try {
                emailService.sendContactEmail(request);
            } catch (Exception e) {
                // Log but don't fail if email sending fails
                System.err.println("Failed to send email: " + e.getMessage());
            }
        }
        
        return savedContact;
    }
    
    public List<Contact> getAllContacts() {
        return contactRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Contact updateStatus(Long id, String statusStr) {
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        
        try {
            ContactStatus status = ContactStatus.valueOf(statusStr);
            contact.setStatus(status);
            return contactRepository.save(contact);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + statusStr);
        }
    }
    
    public List<Contact> getContactsByStatus(ContactStatus status) {
        return contactRepository.findByStatus(status);
    }
}