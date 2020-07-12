package com.CriStru.orurodeliveryapp.Models;

public class Categoria {
    String Nombre;
    String Descripcion;
    String FotoUrl;
    public Categoria() {
    }
    public Categoria(String nombre, String descripcion, String fotoUrl) {
        Nombre = nombre;
        Descripcion = descripcion;
        FotoUrl = fotoUrl;
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
