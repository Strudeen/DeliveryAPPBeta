package com.CriStru.orurodeliveryapp.Models;

public class Producto {
    private String SubCategoria;
    private String Nombre;
    private String Descripcion;
    private String FotoUrl;
    private int Stock;
    private float precio;
    private String IdProducto;

    public Producto() {
    }

    public Producto(String subCategoria, String nombre, String descripcion, String fotoUrl, int stock, float precio, String idProducto) {
        SubCategoria = subCategoria;
        Nombre = nombre;
        Descripcion = descripcion;
        FotoUrl = fotoUrl;
        Stock = stock;
        this.precio = precio;
        IdProducto = idProducto;
    }

    public String getSubCategoria() {
        return SubCategoria;
    }

    public void setSubCategoria(String subCategoria) {
        SubCategoria = subCategoria;
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

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(String idProducto) {
        IdProducto = idProducto;
    }


}
