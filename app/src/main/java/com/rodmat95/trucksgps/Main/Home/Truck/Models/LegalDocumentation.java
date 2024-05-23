package com.rodmat95.trucksgps.Main.Home.Truck.Models;

import java.io.Serializable;

public class LegalDocumentation implements Serializable {
    private String companiaAseguradora;
    private String numeroSOAT;
    private String fechaEmisionSOAT;
    private String fechaVencimientoSOAT;
    private String titularSeguro;
    private String dniTitular;
    private String numeroRevisionTecnica;
    private String fechaEmisionRevisionTecnica;
    private String fechaVencimientoRevisionTecnica;

    public LegalDocumentation() {

    }

    public LegalDocumentation(String companiaAseguradora, String numeroSOAT, String fechaEmisionSOAT, String fechaVencimientoSOAT, String titularSeguro, String dniTitular, String numeroRevisionTecnica, String fechaEmisionRevisionTecnica, String fechaVencimientoRevisionTecnica) {
        this.companiaAseguradora = companiaAseguradora;
        this.numeroSOAT = numeroSOAT;
        this.fechaEmisionSOAT = fechaEmisionSOAT;
        this.fechaVencimientoSOAT = fechaVencimientoSOAT;
        this.titularSeguro = titularSeguro;
        this.dniTitular = dniTitular;
        this.numeroRevisionTecnica = numeroRevisionTecnica;
        this.fechaEmisionRevisionTecnica = fechaEmisionRevisionTecnica;
        this.fechaVencimientoRevisionTecnica = fechaVencimientoRevisionTecnica;
    }

    public String getCompaniaAseguradora() {
        return companiaAseguradora;
    }

    public void setCompaniaAseguradora(String companiaAseguradora) {
        this.companiaAseguradora = companiaAseguradora;
    }

    public String getNumeroSOAT() {
        return numeroSOAT;
    }

    public void setNumeroSOAT(String numeroSOAT) {
        this.numeroSOAT = numeroSOAT;
    }

    public String getFechaEmisionSOAT() {
        return fechaEmisionSOAT;
    }

    public void setFechaEmisionSOAT(String fechaEmisionSOAT) {
        this.fechaEmisionSOAT = fechaEmisionSOAT;
    }

    public String getFechaVencimientoSOAT() {
        return fechaVencimientoSOAT;
    }

    public void setFechaVencimientoSOAT(String fechaVencimientoSOAT) {
        this.fechaVencimientoSOAT = fechaVencimientoSOAT;
    }

    public String getTitularSeguro() {
        return titularSeguro;
    }

    public void setTitularSeguro(String titularSeguro) {
        this.titularSeguro = titularSeguro;
    }

    public String getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }

    public String getNumeroRevisionTecnica() {
        return numeroRevisionTecnica;
    }

    public void setNumeroRevisionTecnica(String numeroRevisionTecnica) {
        this.numeroRevisionTecnica = numeroRevisionTecnica;
    }

    public String getFechaEmisionRevisionTecnica() {
        return fechaEmisionRevisionTecnica;
    }

    public void setFechaEmisionRevisionTecnica(String fechaEmisionRevisionTecnica) {
        this.fechaEmisionRevisionTecnica = fechaEmisionRevisionTecnica;
    }

    public String getFechaVencimientoRevisionTecnica() {
        return fechaVencimientoRevisionTecnica;
    }

    public void setFechaVencimientoRevisionTecnica(String fechaVencimientoRevisionTecnica) {
        this.fechaVencimientoRevisionTecnica = fechaVencimientoRevisionTecnica;
    }
}
