package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;


import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner; 
import java.util.stream.Collectors;

public class AppInicio {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;
    private List<Escritor> autores;
    private List<ObraLiteraria> libros;


    




    private DatosBusqueda getBusqueda() {
        System.out.println("Escribe el nombre del Autor del libro ");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        return datos;

    }

    private void buscarLibro() {
        DatosBusqueda datosBusqueda = getBusqueda();
        if (datosBusqueda != null && !datosBusqueda.resultado().isEmpty()) {
            DatosLibros primerLibro = datosBusqueda.resultado().get(0);


            ObraLiteraria libro = new ObraLiteraria(primerLibro);
            System.out.println("***** ObraLiteraria *****");
            System.out.println(libro);
            System.out.println("*********************");

                Optional<ObraLiteraria> libroExiste = repositoryLibro.findByTitulo(libro.getTitulo());
                if (libroExiste.isPresent()){
                    System.out.println("\nEl libro ya se encuentra registrado\n");
                }else {

                    if (!primerLibro.autor().isEmpty()) {
                        DatosAutor autor = primerLibro.autor().get(0);
                        Escritor autor1 = new Escritor(autor);
                        Optional<Escritor> autorOptional = repositoryAutor.findByNombre(autor1.getNombre());

                        if (autorOptional.isPresent()) {
                            Escritor autorExiste = autorOptional.get();
                            libro.setAutor(autorExiste);
                            repositoryLibro.save(libro);
                        } else {
                            Escritor autorNuevo = repositoryAutor.save(autor1);
                            libro.setAutor(autorNuevo);
                            repositoryLibro.save(libro);
                        }

                        Integer numeroDescargas = libro.getNumero_descargas() != null ? libro.getNumero_descargas() : 0;
                        System.out.println("********** ObraLiteraria **********");
                        System.out.printf("Titulo: %s%nAutor: %s%nIdioma: %s%nNumero de Descargas: %s%n",
                                libro.getTitulo(), autor1.getNombre(), libro.getLenguaje(), libro.getNumero_descargas());
                        System.out.println("***************************\n");
                    } else {
                        System.out.println("Sin autor.");
                    }
                }
        } else {
            System.out.println("El libro no se ha encontrado.");
        }
    }
    private void mostrarLibros() {
        libros = repositoryLibro.findAll();
        libros.stream()
                .forEach(System.out::println);
    }

    private void listarEscritores() {
        autores = repositoryAutor.findAll();
        autores.stream()
                .forEach(System.out::println);
    }

    private void autoresVivosPorAnio() {
        System.out.println("Ingresa el año del autor que desea buscar: ");
        var anioPublicacion = teclado.nextInt();
        autores = repositoryAutor.listaAutoresVivosPorAnio(anioPublicacion);
        autores.stream()
                .forEach(System.out::println);
    }

    private List<ObraLiteraria> datosBusquedaLenguaje(String idioma){
        var dato = Idioma.fromString(idioma);
        System.out.println("Lenguaje buscado: " + dato);

        List<ObraLiteraria> libroPorIdioma = repositoryLibro.findByLenguaje(dato);
        return libroPorIdioma;
    }

    private void buscarLibroPorIdioma(){
        System.out.println("Selecciona el lenguaje/idioma que deseas buscar: ");

        var opcion = -1;
        while (opcion != 0) {
            var opciones = """
                    1. en - Ingles
                    2. es - Español
                    3. fr - Francés
                    4. pt - Portugués
                    
                    0. Volver a Las opciones anteriores
                    """;
            System.out.println(opciones);
            while (!teclado.hasNextInt()) {
                System.out.println("Formato inválido, ingrese un número que esté disponible en el menú");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    List<ObraLiteraria> librosEnIngles = datosBusquedaLenguaje("[en]");
                    librosEnIngles.forEach(System.out::println);
                    break;
                case 2:
                    List<ObraLiteraria> librosEnEspanol = datosBusquedaLenguaje("[es]");
                    librosEnEspanol.forEach(System.out::println);
                    break;
                case 3:
                    List<ObraLiteraria> librosEnFrances = datosBusquedaLenguaje("[fr]");
                    librosEnFrances.forEach(System.out::println);
                    break;
                case 4:
                    List<ObraLiteraria> librosEnPortugues = datosBusquedaLenguaje("[pt]");
                    librosEnPortugues.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Ningún idioma seleccionado");
            }
        }
    }

    private void top10LibrosMasDescargados() {
        List<ObraLiteraria> topLibros = repositoryLibro.top10LibrosMasDescargados();
        topLibros.forEach(System.out::println);
    }

    private void rankingLibro() {
        libros = repositoryLibro.findAll();
        IntSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumero_descargas() > 0)
                .collect(Collectors.summarizingInt(ObraLiteraria::getNumero_descargas));

        ObraLiteraria libroMasDescargado = libros.stream()
                .filter(l -> l.getNumero_descargas() == est.getMax())
                .findFirst()
                .orElse(null);

        ObraLiteraria libroMenosDescargado = libros.stream()
                .filter(l -> l.getNumero_descargas() == est.getMin())
                .findFirst()
                .orElse(null);
        System.out.println("******************************************************");
        System.out.printf("%nLibro más descargado: %s%nNúmero de descargas: " +
                "%d%n%nLibro menos descargado: %s%nNúmero de descargas: " +
                "%d%n%n",libroMasDescargado.getTitulo(),est.getMax(),
                libroMenosDescargado.getTitulo(),est.getMin());
        System.out.println("******************************************************");
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            System.out.println("*********************************\n");
            var menu = """
                    1 - Buscar libros por título
                    2 - Mostrar libros registrados
                    3 - Mostrar autores registrados
                    4 - Autores vivos en determinado año
                    5 - Buscar libros por idioma
                    6 - Top 10 libros más descargados
                    7 - ObraLiteraria más descargado y menos descargado 
                    
                    0 - Salir
                    
                    """;


            System.out.println(menu);
            while (!teclado.hasNextInt()) {
                System.out.println("Formato inválido, ingrese un número que este disponible en el menú!");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    listarEscritores();
                    break;
                case 4:
                    autoresVivosPorAnio();
                    break;
                case 5:
                    buscarLibroPorIdioma();
                    break;
                case 6:
                    top10LibrosMasDescargados();
                    break;
                case 7:
                    rankingLibro();
                    break;
                case 0:
                    System.out.println("Finalizando  la aplicación");
                    break;
                default:
                    System.out.printf("Opción inválida\n");
            }
        }
    }
    public AppInicio(LibroRepository repositoryLibro, AutorRepository repositoryAutor) {
        this.repositoryLibro = repositoryLibro;
        this.repositoryAutor = repositoryAutor;
    }
}






