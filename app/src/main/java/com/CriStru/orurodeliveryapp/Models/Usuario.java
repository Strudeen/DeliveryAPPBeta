package com.CriStru.orurodeliveryapp.Models;

public class Usuario {
    String nombre;
    String apellido;
    String celular;

    public Usuario() {
    }

    String tipo;

    public Usuario(String nombre, String apellido, String celular, String tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
