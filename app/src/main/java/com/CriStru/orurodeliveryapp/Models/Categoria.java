package com.CriStru.orurodeliveryapp.Models;

public class Categoria {
    private String categoria_id;
    private String Nombre;
    private String Descripcion;
    private String FotoUrl;

    public Categoria() {
    }

    public Categoria(String categoria_id, String Nombre, String Descripcion, String FotoUrl) {
        this.categoria_id = categoria_id;
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.FotoUrl = FotoUrl;
    }
    public Categoria(String Nombre, String Descripcion, String FotoUrl) {
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.FotoUrl = FotoUrl;
    }

    public Categoria(String categoria_id, String FotoUrl) {
        this.categoria_id = categoria_id;
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.FotoUrl = FotoUrl;
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

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getFotoUrl() {
        return FotoUrl;
    }

    public void setFotoUrl(String FotoUrl) {
        this.FotoUrl = FotoUrl;
    }
}