package com.CriStru.orurodeliveryapp.Adapters.Ubicaciones;

public class Ubicaciones {

    private String idUbicacion;
    private String nombreUbicacion;

    public String getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(String idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }

    public Ubicaciones(){

    }

    public Ubicaciones(String idUbicacion, String nombreUbicacion) {
        this.idUbicacion = idUbicacion;
        this.nombreUbicacion = nombreUbicacion;
    }
}
