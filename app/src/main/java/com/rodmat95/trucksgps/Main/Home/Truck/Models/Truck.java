package com.rodmat95.trucksgps.Main.Home.Truck.Models;

import java.io.Serializable;

public class Truck implements Serializable {
    private String uniqueIdentifier;
    private Information informacion;
    private LegalDocumentation documentacionLegal;
    private Details detalles;

    public Truck() {

    }

    public Truck(String uniqueIdentifier, Information informacion, LegalDocumentation documentacionLegal, Details detalles) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.informacion = informacion;
        this.documentacionLegal = documentacionLegal;
        this.detalles = detalles;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public Information getInformacion() {
        return informacion;
    }

    public void setInformacion(Information informacion) {
        this.informacion = informacion;
    }

    public LegalDocumentation getDocumentacionLegal() {
        return documentacionLegal;
    }

    public void setDocumentacionLegal(LegalDocumentation documentacionLegal) {
        this.documentacionLegal = documentacionLegal;
    }

    public Details getDetalles() {
        return detalles;
    }

    public void setDetalles(Details detalles) {
        this.detalles = detalles;
    }
}