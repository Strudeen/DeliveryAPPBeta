package com.CriStru.orurodeliveryapp.Models;

public class Categoria {
    String categoria_id;
    String Nombre;
    String Descripcion;
    String FotoUrl;

    public Categoria() {
    }

    public Categoria(String categoria_id, String nombre, String descripcion, String fotoUrl) {
        this.categoria_id = categoria_id;
        Nombre = nombre;
        Descripcion = descripcion;
        FotoUrl = fotoUrl;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFotoUrl() {
        return FotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        FotoUrl = fotoUrl;
    }
}
