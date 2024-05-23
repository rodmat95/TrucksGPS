package com.rodmat95.trucksgps.Main.Home.Truck.Models;

import java.io.Serializable;

public class Details implements Serializable {
    private String fechaCompraVehiculo;
    private String valorCompraVehiculo;
    private String colorVehiculo;
    private String pesoVehiculo;
    private String longitudVehiculo;
    private String anchuraVehiculo;
    private String alturaVehiculo;

    public Details() {

    }

    public Details(String fechaCompraVehiculo, String valorCompraVehiculo, String colorVehiculo, String pesoVehiculo, String longitudVehiculo, String anchuraVehiculo, String alturaVehiculo) {
        this.fechaCompraVehiculo = fechaCompraVehiculo;
        this.valorCompraVehiculo = valorCompraVehiculo;
        this.colorVehiculo = colorVehiculo;
        this.pesoVehiculo = pesoVehiculo;
        this.longitudVehiculo = longitudVehiculo;
        this.anchuraVehiculo = anchuraVehiculo;
        this.alturaVehiculo = alturaVehiculo;
    }

    public String getFechaCompraVehiculo() {
        return fechaCompraVehiculo;
    }

    public void setFechaCompraVehiculo(String fechaCompraVehiculo) {
        this.fechaCompraVehiculo = fechaCompraVehiculo;
    }

    public String getValorCompraVehiculo() {
        return valorCompraVehiculo;
    }

    public void setValorCompraVehiculo(String valorCompraVehiculo) {
        this.valorCompraVehiculo = valorCompraVehiculo;
    }

    public String getColorVehiculo() {
        return colorVehiculo;
    }

    public void setColorVehiculo(String colorVehiculo) {
        this.colorVehiculo = colorVehiculo;
    }

    public String getPesoVehiculo() {
        return pesoVehiculo;
    }

    public void setPesoVehiculo(String pesoVehiculo) {
        this.pesoVehiculo = pesoVehiculo;
    }

    public String getLongitudVehiculo() {
        return longitudVehiculo;
    }

    public void setLongitudVehiculo(String longitudVehiculo) {
        this.longitudVehiculo = longitudVehiculo;
    }

    public String getAnchuraVehiculo() {
        return anchuraVehiculo;
    }

    public void setAnchuraVehiculo(String anchuraVehiculo) {
        this.anchuraVehiculo = anchuraVehiculo;
    }

    public String getAlturaVehiculo() {
        return alturaVehiculo;
    }

    public void setAlturaVehiculo(String alturaVehiculo) {
        this.alturaVehiculo = alturaVehiculo;
    }
}
