package com.CriStru.orurodeliveryapp.Adapters.Scroll;

public class Promociones {

    private String id;
    private String fotoUrl;

    public Promociones() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Promociones(String id, String fotoUrl) {
        this.id = id;
        this.fotoUrl = fotoUrl;
    }
}
