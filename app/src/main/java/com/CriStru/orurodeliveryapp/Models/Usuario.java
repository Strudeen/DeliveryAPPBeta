package com.CriStru.orurodeliveryapp.Models;

public class Usuario {
    String nombre;
    String apellido;
    String ci;

    public Usuario() {
    }

    String tipo;

    public Usuario(String nombre, String apellido, String ci, String tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.ci = ci;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
