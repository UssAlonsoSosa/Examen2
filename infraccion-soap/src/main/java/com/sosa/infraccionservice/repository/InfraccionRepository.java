package com.sosa.infraccionservice.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sosa.infraccionservice.entity.infracciones;

@Repository
public interface InfraccionRepository extends JpaRepository<infracciones, Integer> {
    List<infracciones> findByNombreContaining(String nombre, Pageable page);
    infracciones findByNombre(String nombre);
}