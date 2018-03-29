package me.flux.fluxme.Business;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by JosueAndroid on 27/3/2018.
 */

public class Usuario_Singleton {
    private static final Usuario_Singleton ourInstance = new Usuario_Singleton();

    private String nombre;
    private String username;
    private String foto;
    private boolean admin;

    private ArrayList<String> emisorasFavs = new ArrayList<String>();

    public static Usuario_Singleton getInstance() {
        return ourInstance;
    }

    private Usuario_Singleton() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ArrayList<String> getEmisorasFavs() {
        return emisorasFavs;
    }

    public void setEmisorasFavs(ArrayList<String> emisorasFavs) {
        this.emisorasFavs = emisorasFavs;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
