package com.CriStru.orurodeliveryapp.Models;

public class Usuario {
    String nombre;
    String celular;

    public Usuario() {
    }

    String tipo;

    public Usuario(String nombre, String celular, String tipo) {
        this.nombre = nombre;
        this.celular = celular;
        this.tipo = tipo;
    }
    public Usuario(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

