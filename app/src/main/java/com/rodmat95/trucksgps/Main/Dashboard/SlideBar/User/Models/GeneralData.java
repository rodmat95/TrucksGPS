package com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models;

import java.io.Serializable;

public class GeneralData implements Serializable {
    private String contraseña;
    private String correo;
    private String usuario;
    private String rol;

    public GeneralData() {

    }

    public GeneralData(String contraseña, String correo, String usuario, String rol) {
        this.contraseña = contraseña;
        this.correo = correo;
        this.usuario = usuario;
        this.rol = rol;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}