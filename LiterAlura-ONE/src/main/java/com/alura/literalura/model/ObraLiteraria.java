package com.alura.literalura.model;


import jakarta.persistence.*;


@Entity
@Table(name = "libros")
public class ObraLiteraria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombreObra;
    @ManyToOne
    private Escritor autor;
    @Enumerated(EnumType.STRING)
    private Idioma lenguaje;
    private Integer numero_descargas;

    public ObraLiteraria(){}

    public ObraLiteraria(DatosLibros datosLibro){
        this.nombreObra = datosLibro.nombreObra();
        this.lenguaje = Idioma.fromString(datosLibro.idiomas().toString().split(",")[0].trim());
        this.numero_descargas = datosLibro.numero_descargas();
    }

    public void setNumero_descargas(Integer numero_descargas) {
        this.numero_descargas = numero_descargas;
    }
    public Integer getNumero_descargas() {
        return numero_descargas;
    }
    public void setLenguaje(Idioma lenguaje) {
        this.lenguaje = lenguaje;
    }
    public Idioma getLenguaje() {
        return lenguaje;
    }
    public void setAutor(Escritor autor) {
        this.autor = autor;
    }
    public Escritor getAutor() {
        return autor;
    }
    public void setTitulo(String nombreObra) {
        this.nombreObra = nombreObra;
    }
    public String getTitulo() {
        return nombreObra;
    }
    public void setId(Long codigoUnico) {
        Id = codigoUnico;
    }
    public Long getId() {
        return Id;
    }
    public String toString() {
        String nombreAutor = (autor != null) ? autor.getNombre() : "Escritor desconocido";
        return String.format("********** ObraLiteraria **********%nTitulo:" +
                " %s%nAutor: %s%nIdioma: %s%nNumero de Descargar:" +
                " %d%n***************************%n",nombreObra,nombreAutor,lenguaje,numero_descargas);
    }
}
