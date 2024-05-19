package com.tfg.scrumweb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.scrumweb.controller.Autenticador;
import com.tfg.scrumweb.dto.EmpresaDTO;
import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Empresa;
import com.tfg.scrumweb.entity.Imagen;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.entity.Tecnologia;
import com.tfg.scrumweb.entity.Usuario;
import com.tfg.scrumweb.enumeradores.Estado;
import com.tfg.scrumweb.enumeradores.Rol;
import com.tfg.scrumweb.exception.EmailAlreadyExists;
import com.tfg.scrumweb.exception.ItemNotFoundException;
import com.tfg.scrumweb.exception.NotAllowedException;
import com.tfg.scrumweb.exception.ScrumViolation;
import com.tfg.scrumweb.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService{
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imageService;

    @Autowired
    private TecnologiaService tecnologiaService;

    @Transactional
    public Usuario save(Usuario u){
        return userRepository.save(u);
    }
    public Usuario findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Usuario findById(Long id) throws ItemNotFoundException{
        Optional<Usuario> oUser = userRepository.findById(id);
        if(!oUser.isPresent())
            throw new ItemNotFoundException("Usuario no encontrado");
        else
            return oUser.get();
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = userRepository.findByEmail(email);
        logger.info("En el load");
        if (user == null) {
             throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
    public Persona crearPersona(Persona p){
        return (Persona) this.save(p);
    }
    public Imagen updateImagen(Long id, MultipartFile multipartImage) throws IOException, ItemNotFoundException{
        Usuario p = this.findById(id);
        if(p.getImg() == null){
            Imagen image2 = imageService.save(new Imagen(multipartImage.getBytes(), multipartImage.getOriginalFilename(), p));
            p.setImg(image2);
        }
        else{
            p.getImg().setContent(multipartImage.getBytes());
            p.getImg().setName(multipartImage.getName());
        }
        return p.getImg();
    }
    public Persona updatePersona(Long id, Persona p) throws ItemNotFoundException, EmailAlreadyExists, ScrumViolation, NotAllowedException {
        Persona modificar = (Persona) this.findById(id);
        Usuario u = Autenticador.autenticador();
        if((u instanceof Empresa) && (!modificar.getEmpresa().equals(u)))
            throw new NotAllowedException("You can't modify people of other business");
            
        if((u instanceof Persona) && (!modificar.equals(u)))
            throw new NotAllowedException("You can't modify other people");

        Persona pAntigua = (Persona) this.findById(id);
        if(!(pAntigua.getEmail().equals(p.getEmail())) && (this.findByEmail(p.getEmail()) != null))
            throw new EmailAlreadyExists("A user with that email already exists");
        if((pAntigua.getRol().equals(Rol.SCRUM_MASTER) || pAntigua.getRol().equals(Rol.PRODUCT_OWNER)) && !(p.getRol().equals(pAntigua.getRol())))
            throw new ScrumViolation("There must always be only one Product Owner and Scrum Master in each project");

        pAntigua.setUsername(p.getUsername());
        pAntigua.setEmail(p.getEmail());
        if(p.getScrumSubTeam() != null)
            pAntigua.setScrumSubTeam(p.getScrumSubTeam());
        if(p.getPass() != null)
            pAntigua.setPassYaEncodeada(p.getPass());
        
            
        pAntigua.setRol(p.getRol());
        List<Tecnologia> aBorrar = new ArrayList<>(pAntigua.getTecnologias());
        for(Tecnologia t: p.getTecnologias()){
            if(t.getId()==0){
                t.setPersona(pAntigua);
                Tecnologia t2 = tecnologiaService.save(t);
                pAntigua.anadirTecnologia(t2);
            }
            else{
                Tecnologia tAntigua = tecnologiaService.findById(t.getId());
                tAntigua.setCategoria(t.getCategoria());
                tAntigua.setItem(t.getItem());
                aBorrar.remove(tAntigua);
                tecnologiaService.save(tAntigua);
            }
        }
        tecnologiaService.deleteAll(pAntigua, aBorrar);
        return (Persona) this.save(pAntigua);
    }
    public Empresa updateEmpresa(Long id, EmpresaDTO empresa) throws ItemNotFoundException, NotAllowedException{
        Usuario u = Autenticador.autenticador();
        Empresa empresaAntigua = (Empresa) this.findById(id);
        if(!u.equals(empresaAntigua))
            throw new NotAllowedException("You can't modify other business");
        empresaAntigua.setUsername(empresa.getUsername());
        empresaAntigua.setEmail(empresa.getEmail());
        if(!empresa.getPass().equals(""))
            empresaAntigua.setPassYaEncodeada(empresa.getPass());
        return empresaAntigua;
    }
    public List<Persona> sinProyecto(){
        List<Persona> toReturn = new ArrayList<>();
        for(Usuario u: userRepository.findAll()){
            if(u instanceof Persona){
                Persona paux = (Persona) u;
                if(paux.getProyecto() == null)
                    toReturn.add(paux);
            }
        }
        return toReturn;
    }

    public void deleteAll(List<Persona> l){
        userRepository.deleteAll(l);
    }
    public void delete(Persona l){
        userRepository.delete(l);
    }

    public void deleteById(Long id) throws ItemNotFoundException, ScrumViolation{
        Usuario user = this.findById(id);
        if(user instanceof Persona){
            Persona persona = (Persona) user;
            if(persona.getRol().equals(Rol.PRODUCT_OWNER) || (persona.getRol().equals(Rol.SCRUM_MASTER)))
                throw new ScrumViolation("There must always be only one Product Owner and Scrum Master in each project");
            for(BacklogItem b: persona.getListaBacklogItem()){
                logger.info("4");
                b.setEncargado(null);
                b.setDificultad(null);
                b.setEstado(Estado.TODO);
            }
            persona.getProyecto().getPersonas().remove(persona);
            persona.getEmpresa().getEmpleados().remove(persona);
            tecnologiaService.deleteAll(persona, persona.getTecnologias());
            
            
            persona.setListaBacklogItem(null);
        }
        //Borramos foto antes
        if(user.getImg() != null)
            imageService.delete(user.getImg());
        user.setImg(null);
        userRepository.deleteById(id);
    }
    public List<Persona> findByProyectoId(long id){
        return userRepository.findByProyectoId(id);
    }
}
