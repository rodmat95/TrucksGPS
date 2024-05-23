package com.rodmat95.trucksgps.Main.Home.Driver.Models;

import java.io.Serializable;

public class PersonalData implements Serializable {
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String numeroTelefono;
    private String fechaNacimiento;
    private String direccionResidencia;
    private String nombreContacto;
    private String numeroContacto;

    public PersonalData(){

    }

    public PersonalData(String nombreCompleto, String tipoDocumento, String numeroDocumento, String numeroTelefono, String fechaNacimiento, String direccionResidencia, String nombreContacto, String numeroContacto){
        this.nombreCompleto = nombreCompleto;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.numeroTelefono = numeroTelefono;
        this.fechaNacimiento = fechaNacimiento;
        this.direccionResidencia = direccionResidencia;
        this.nombreContacto = nombreContacto;
        this.numeroContacto = numeroContacto;
    }

    /*
    private Map<String, String> datos;

    public Map<String, String> getDatos() {
        return datos;
    }

    public void setDatos(Map<String, String> datos) {
        this.datos = datos;
    }
    */

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    public void setDireccionResidencia(String direccionResidencia) {
        this.direccionResidencia = direccionResidencia;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }
}