package com.rodmat95.trucksgps.Main.Home.Driver.Models;

import java.io.Serializable;

public class Driver implements Serializable {
    private String uniqueIdentifier;
    private PersonalData datosPersonales;
    private LegalDocumentation documentacionLegal;

    public Driver() {

    }

    public Driver(String uniqueIdentifier, PersonalData datosPersonales, LegalDocumentation documentacionLegal) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.datosPersonales = datosPersonales;
        this.documentacionLegal = documentacionLegal;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public PersonalData getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(PersonalData datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    public LegalDocumentation getDocumentacionLegal() {
        return documentacionLegal;
    }

    public void setDocumentacionLegal(LegalDocumentation documentacionLegal) {
        this.documentacionLegal = documentacionLegal;
    }
}