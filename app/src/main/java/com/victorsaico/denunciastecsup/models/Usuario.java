package com.victorsaico.denunciastecsup.models;

/**
 * Created by JARVIS on 13/11/2017.
 */

public class Usuario {


    private int id;
    private String username;
    private String correo;
    private String imagen;

    public Usuario(int id, String username, String correo, String imagen) {
        this.id = id;
        this.username = username;
        this.correo = correo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", correo='" + correo + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
