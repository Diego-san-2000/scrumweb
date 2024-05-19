package com.tfg.scrumweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.scrumweb.entity.Imagen;
import com.tfg.scrumweb.repository.ImagenRepository;

@Service
public class ImagenService {
    @Autowired
    private ImagenRepository imageRepository;

    public Imagen save(Imagen s){
        return imageRepository.save(s);
    }
    public Imagen findById(Long id){
        return imageRepository.findById(id).get();
    }
    public void delete(Imagen i){
        imageRepository.delete(i);
    }
}
