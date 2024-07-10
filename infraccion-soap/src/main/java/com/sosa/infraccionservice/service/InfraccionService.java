package com.sosa.infraccionservice.service;



import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import com.sosa.infraccionservice.entity.infracciones;
import com.sosa.infraccionservice.repository.InfraccionRepository;

@Service
@Slf4j
public class InfraccionService {
    @Autowired
    private InfraccionRepository repository;
    
    @Transactional(readOnly=true)
    public List<infracciones> findAll(Pageable page) {
        try {
            return repository.findAll(page).toList();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional(readOnly=true)
    public List<infracciones> findByNombre(String nombre, Pageable page) {
        try {
            return repository.findByNombreContaining(nombre, page);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional(readOnly=true)
    public infracciones findById(int id) {
        try {
        	infracciones registro = repository.findById(id).orElseThrow();
            return registro;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional
    public infracciones save(infracciones infraccion) {
        try {
            if(repository.findByNombre(infraccion.getDni()) != null) {
                return null;
            }
            infraccion.setCreatedAt(new Date());
            infraccion.setUpdatedAt(new Date());
            infracciones nuevo = repository.save(infraccion);
            return nuevo;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public infracciones update(infracciones infraccion) {
        try {
        	infracciones registro = repository.findById(infraccion.getId()).orElseThrow();
        	infracciones registroD = repository.findByNombre(infraccion.getDni());
            
            if(registroD != null && infraccion.getId() != registroD.getId()) {
                return null;
            }
            
            registro.setDni(infraccion.getDni());
            registro.setDescripcion(infraccion.getDescripcion());
            registro.setEstado(infraccion.getEstado());
            registro.setTipoInfraccion(infraccion.getTipoInfraccion());
            registro.setFecha(infraccion.getFecha());
            registro.setMontoMulta(infraccion.getMontoMulta());
            registro.setUbicacion(infraccion.getUbicacion());
            registro.setUpdatedAt(new Date());
            repository.save(registro);
            return registro;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional
    public boolean delete(int id) {
        try {
        	infracciones infraccion = repository.findById(id).orElseThrow();
            repository.delete(infraccion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

