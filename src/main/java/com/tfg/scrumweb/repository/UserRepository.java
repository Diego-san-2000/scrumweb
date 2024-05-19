package com.tfg.scrumweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    /**
     * Find users by their last name.
     *
     * @param username The last name to search for.
     * @return A list of Usuario entities with the given last name.
     */
    List<Usuario> findByUsername(String username);

    /**
     * Find a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return The Usuario entity with the specified ID, or null if not found.
     */
    Usuario findById(long id);
    Usuario findByEmail(String email);
    List<Persona> findByProyectoId(long id);
}