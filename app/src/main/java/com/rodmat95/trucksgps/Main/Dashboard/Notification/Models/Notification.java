package com.rodmat95.trucksgps.Main.Dashboard.Notification.Models;

import java.io.Serializable;

public class Notification implements Serializable {
    private String uniqueIdentifier;
    private String usuario;
    private String accion;
    private String atributo;
    private String objeto;
    private String distintivo;
    private String distintivoValor;
    private String atributoValorAnterior;
    private String atributoValorNuevo;
    private String fecha;
    private String idu;

    public Notification() {

    }

    public Notification(String uniqueIdentifier, String usuario, String accion, String atributo, String objeto, String distintivo, String distintivoValor, String atributoValorAnterior, String atributoValorNuevo, String fecha, String idu) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.usuario = usuario;
        this.accion = accion;
        this.atributo = atributo;
        this.objeto = objeto;
        this.distintivo = distintivo;
        this.distintivoValor = distintivoValor;
        this.atributoValorAnterior = atributoValorAnterior;
        this.atributoValorNuevo = atributoValorNuevo;
        this.fecha = fecha;
        this.idu = idu;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getDistintivo() {
        return distintivo;
    }

    public void setDistintivo(String distintivo) {
        this.distintivo = distintivo;
    }

    public String getDistintivoValor() {
        return distintivoValor;
    }

    public void setDistintivoValor(String distintivoValor) {
        this.distintivoValor = distintivoValor;
    }

    public String getAtributoValorAnterior() {
        return atributoValorAnterior;
    }

    public void setAtributoValorAnterior(String atributoValorAnterior) {
        this.atributoValorAnterior = atributoValorAnterior;
    }

    public String getAtributoValorNuevo() {
        return atributoValorNuevo;
    }

    public void setAtributoValorNuevo(String atributoValorNuevo) {
        this.atributoValorNuevo = atributoValorNuevo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdu() {
        return idu;
    }

    public void setIdu(String idu) {
        this.idu = idu;
    }
}