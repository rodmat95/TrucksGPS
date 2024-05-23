package com.rodmat95.trucksgps.Main.Home.Truck.Models;

import java.io.Serializable;

public class Information implements Serializable {
    private String numeroPlaca;
    private String marca;
    private String modelo;
    private String anioProduccion;
    private String numeroMotor;
    private String serieChasis;
    private String tipoCombustible;

    public Information() {

    }

    public Information(String numeroPlaca, String marca, String modelo, String anioProduccion, String numeroMotor, String serieChasis, String tipoCombustible) {
        this.numeroPlaca = numeroPlaca;
        this.marca = marca;
        this.modelo = modelo;
        this.anioProduccion = anioProduccion;
        this.numeroMotor = numeroMotor;
        this.serieChasis = serieChasis;
        this.tipoCombustible = tipoCombustible;

    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnioProduccion() {
        return anioProduccion;
    }

    public void setAnioProduccion(String anioProduccion) {
        this.anioProduccion = anioProduccion;
    }

    public String getNumeroMotor() {
        return numeroMotor;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public String getSerieChasis() {
        return serieChasis;
    }

    public void setSerieChasis(String serieChasis) {
        this.serieChasis = serieChasis;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }
}
