package com.alura.literalura.model;


import jakarta.persistence.*;

import java.util.List;




@Entity
@Table(name = "autores")
public class Escritor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoUnico;
    @Column(unique = true)
    private String nombreAutor;
    private Integer fecha_nacimiento;
    private Integer fecha_deceso;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ObraLiteraria> libro;

    public Escritor(){}

    public Escritor(DatosAutor datosAutor){
        this.nombreAutor=datosAutor.nombreAutor();
        this.fecha_nacimiento = datosAutor.fechaNacimiento();
        this.fecha_deceso = datosAutor.fechaFallecimiento();
    }

    public void setLibro(List<ObraLiteraria> libro) {
        this.libro = libro;
    }
    public List<ObraLiteraria> getLibro() {
        return libro;
    }
    public void setFecha_deceso(Integer fecha_deceso) {
        this.fecha_deceso = fecha_deceso;
    }
    public Integer getFecha_deceso() {
        return fecha_deceso;
    }
    public void setFecha_nacimiento(Integer fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
    public Integer getFecha_nacimiento() {
        return fecha_nacimiento;
    }
    public void setNombre(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }
    public String getNombre() {
        return nombreAutor;
    }
    public void setId(Long codigoUnico) {
        this.codigoUnico = codigoUnico;
    }
    public Long getId() {
        return codigoUnico;
    }
    public String toString() {
        StringBuilder librosStr = new StringBuilder();
        librosStr.append("Libros: ");

        for(int i = 0; i < libro.size() ; i++) {
            librosStr.append(libro.get(i).getTitulo());
            if (i < libro.size() - 1 ){
                librosStr.append(", ");
            }
        }
        return String.format("********** Escritor **********%nNombre:" +
                " %s%n%s%nFecha de Nacimiento: %s%nFecha de Deceso:" +
                " %s%n***************************%n",nombreAutor,librosStr.toString(),fecha_nacimiento,fecha_deceso);
    }
}
