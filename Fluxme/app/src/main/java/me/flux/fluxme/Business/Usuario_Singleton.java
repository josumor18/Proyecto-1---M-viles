package me.flux.fluxme.Business;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by JosueAndroid on 27/3/2018.
 */

public class Usuario_Singleton {
    private static final Usuario_Singleton ourInstance = new Usuario_Singleton();

    private String id;
    private String nombre;
    private String email;
    private String foto;
    private String auth_token;
    private boolean admin = false;//MODIFICAR ESTE MAE SEGUN SI EL USUARIO ES ADMIN DE LA EMISORA CONSULTADA

    private ArrayList<String> emisorasFavs = new ArrayList<String>();

    public static Usuario_Singleton getInstance() {
        return ourInstance;
    }

    private Usuario_Singleton() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email= username;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
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
