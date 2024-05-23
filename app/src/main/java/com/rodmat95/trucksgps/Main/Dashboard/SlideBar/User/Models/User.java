package com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String uniqueIdentifier;
    private GeneralData datosGenerales;
    private PersonalData datosPersonales;

    public User() {

    }

    public User(String uniqueIdentifier, GeneralData datosGenerales, PersonalData datosPersonales) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.datosGenerales = datosGenerales;
        this.datosPersonales = datosPersonales;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public GeneralData getDatosGenerales() {
        return datosGenerales;
    }

    public void setDatosGenerales(GeneralData datosGenerales) {
        this.datosGenerales = datosGenerales;
    }

    public PersonalData getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(PersonalData datosPersonales) {
        this.datosPersonales = datosPersonales;
    }
}