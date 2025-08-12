package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.ObraLiteraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<ObraLiteraria, Long> {
    List<ObraLiteraria> findByLenguaje(Idioma idioma);

    Optional<ObraLiteraria> findByTitulo(String nombreObra);

    @Query("SELECT l FROM ObraLiteraria l ORDER BY l.numero_descargas DESC LIMIT 10")
    List<ObraLiteraria> top10LibrosMasDescargados();
}
