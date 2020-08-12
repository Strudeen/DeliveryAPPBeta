package com.CriStru.orurodeliveryapp.Models;

import com.orm.SugarRecord;

public class Carrito extends SugarRecord {

    private String Nombre;
    private String FotoUrl;
    private int Stock;
    private int MaxStock;
    private float precio;
    private String IdProducto;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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

    public int getMaxStock() {
        return MaxStock;
    }

    public void setMaxStock(int maxStock) {
        MaxStock = maxStock;
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

    public Carrito(){

    }

    public Carrito(String nombre, String fotoUrl, int stock, int maxStock, float precio, String idProducto){
        Nombre = nombre;
        FotoUrl = fotoUrl;

        Stock = stock;
        MaxStock = maxStock;
        this.precio = precio;
        IdProducto = idProducto;
    }


}
