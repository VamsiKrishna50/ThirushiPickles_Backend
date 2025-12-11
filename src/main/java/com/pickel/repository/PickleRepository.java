package com.pickel.repository;

import com.pickel.entity.Pickle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickleRepository extends JpaRepository<Pickle, Long> {
    List<Pickle> findByAvailableTrue();
    List<Pickle> findByCategory(Pickle.Category category);
}