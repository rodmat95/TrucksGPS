package com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models;

import java.io.Serializable;

public class PersonalData implements Serializable {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numero;

    public PersonalData() {

    }

    public PersonalData(String nombre, String apellidoPaterno, String apellidoMaterno, String numero) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
