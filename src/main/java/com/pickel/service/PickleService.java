package com.pickel.service;

import com.pickel.dto.PickleRequest;
import com.pickel.entity.Pickle;
import com.pickel.exception.ResourceNotFoundException;
import com.pickel.repository.PickleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PickleService {
    
    @Autowired
    private PickleRepository pickleRepository;
    
    public List<Pickle> getAllPickles() {
        return pickleRepository.findAll();
    }
    
    public List<Pickle> getAvailablePickles() {
        return pickleRepository.findByAvailableTrue();
    }
    
    public Pickle getPickleById(Long id) {
        return pickleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pickle not found with id: " + id));
    }
    
    @Transactional
    public Pickle createPickle(PickleRequest request) {
        Pickle pickle = new Pickle();
        pickle.setName(request.getName());
        pickle.setDescription(request.getDescription());
        pickle.setPrice(request.getPrice());
        pickle.setStockQuantity(request.getStockQuantity());
        pickle.setBaseWeight(request.getBaseWeight() != null ? request.getBaseWeight() : 250);
        pickle.setImageUrl(request.getImageUrl());
        pickle.setCategory(request.getCategory());
        pickle.setAvailable(true);
        
        return pickleRepository.save(pickle);
    }
    
    @Transactional
    public Pickle updatePickle(Long id, PickleRequest request) {
        Pickle pickle = getPickleById(id);
        
        pickle.setName(request.getName());
        pickle.setDescription(request.getDescription());
        pickle.setPrice(request.getPrice());
        pickle.setStockQuantity(request.getStockQuantity());
        pickle.setBaseWeight(request.getBaseWeight() != null ? request.getBaseWeight() : 250);
        pickle.setImageUrl(request.getImageUrl());
        pickle.setCategory(request.getCategory());
        
        return pickleRepository.save(pickle);
    }
    
    @Transactional
    public void deletePickle(Long id) {
        Pickle pickle = getPickleById(id);
        pickleRepository.delete(pickle);
    }
    
    @Transactional
    public Pickle toggleAvailability(Long id) {
        Pickle pickle = getPickleById(id);
        pickle.setAvailable(!pickle.getAvailable());
        return pickleRepository.save(pickle);
    }
}