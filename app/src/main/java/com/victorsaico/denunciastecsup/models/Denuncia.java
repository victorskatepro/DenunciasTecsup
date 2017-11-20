package com.victorsaico.denunciastecsup.models;

/**
 * Created by JARVIS on 15/11/2017.
 */

public class Denuncia {


    private int id;
    private String titulo;
    private String autor;
    private double latitud;
    private double longitud;
    private String direccion;
    private int usuario_id;
    private String imagen;
    private String descripcion;

    public Denuncia(int id, String titulo, String autor, double latitud, double longitud, String direccion, int usuario_id, String imagen, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.usuario_id = usuario_id;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Denuncia{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", direccion='" + direccion + '\'' +
                ", usuario_id=" + usuario_id +
                ", imagen='" + imagen + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
