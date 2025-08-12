package com.alura.literalura.repository;

import com.alura.literalura.model.Escritor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;


public interface AutorRepository extends JpaRepository<Escritor, Long> {
    Optional<Escritor> findByNombre(String nombreAutor);

    @Query("SELECT a FROM Escritor a WHERE a.fecha_nacimiento <= :anioPublicacion AND a.fecha_deceso >= :anioPublicacion")
    List<Escritor> listaAutoresVivosPorAnio(Integer anioPublicacion);
}
