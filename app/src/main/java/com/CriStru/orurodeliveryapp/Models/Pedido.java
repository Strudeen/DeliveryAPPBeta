package com.CriStru.orurodeliveryapp.Models;

public class Pedido {
    private String id;
    private boolean completado;
    private String dly;

    public Pedido(String id, boolean completado, String dly) {
        this.id = id;
        this.completado = completado;
        this.dly = dly;
    }
    public Pedido(String id, String dly){
        this.id = id;
        this.dly = dly;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public String getDly() {
        return dly;
    }

    public void setDly(String dly) {
        this.dly = dly;
    }
}
