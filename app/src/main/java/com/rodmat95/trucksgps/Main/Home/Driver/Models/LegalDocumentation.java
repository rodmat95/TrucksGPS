package com.rodmat95.trucksgps.Main.Home.Driver.Models;

import java.io.Serializable;

public class LegalDocumentation implements Serializable {
    private String numeroLicencia;
    private String claseLicencia;
    private String categoriaLicencia;
    private String fechaExpedicion;
    private String fechaRevalidacion;
    private String autorizacionesEspeciales;
    private String restricciones;

    public LegalDocumentation(){

    }

    public LegalDocumentation(String numeroLicencia, String claseLicencia, String categoriaLicencia, String fechaExpedicion, String fechaRevalidacion, String autorizacionesEspeciales, String restricciones){
        this.numeroLicencia = numeroLicencia;
        this.claseLicencia = claseLicencia;
        this.categoriaLicencia = categoriaLicencia;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaRevalidacion = fechaRevalidacion;
        this.autorizacionesEspeciales = autorizacionesEspeciales;
        this.restricciones = restricciones;
    }

    /*
    private Map<String, String> documentacion;

    public Map<String, String> getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(Map<String, String> documentacion) {
        this.documentacion = documentacion;
    }
    */

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getClaseLicencia() {
        return claseLicencia;
    }

    public void setClaseLicencia(String claseLicencia) {
        this.claseLicencia = claseLicencia;
    }

    public String getCategoriaLicencia() {
        return categoriaLicencia;
    }

    public void setCategoriaLicencia(String categoriaLicencia) {
        this.categoriaLicencia = categoriaLicencia;
    }

    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getFechaRevalidacion() {
        return fechaRevalidacion;
    }

    public void setFechaRevalidacion(String fechaRevalidacion) {
        this.fechaRevalidacion = fechaRevalidacion;
    }

    public String getAutorizacionesEspeciales() {
        return autorizacionesEspeciales;
    }

    public void setAutorizacionesEspeciales(String autorizacionesEspeciales) {
        this.autorizacionesEspeciales = autorizacionesEspeciales;
    }

    public String getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }
}