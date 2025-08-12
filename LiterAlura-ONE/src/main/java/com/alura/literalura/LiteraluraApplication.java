package com.alura.literalura;

import com.alura.literalura.principal.AppInicio;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository repositoryLibro;
	@Autowired
	private AutorRepository repositoryAutor;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
			AppInicio principal = new AppInicio(repositoryLibro, repositoryAutor);
			principal.muestraElMenu();

		}
}

