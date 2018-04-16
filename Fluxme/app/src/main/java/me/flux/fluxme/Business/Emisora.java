package me.flux.fluxme.Business;

import android.graphics.Bitmap;

/**
 * Created by JosueAndroid on 13/4/2018.
 */

public class Emisora {
    private int id;
    private String nombre;
    private String link;
    private int id_admin;



    private String descripcion;

    public Emisora(int id, String nombre, String link, int id_admin, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.id_admin = id_admin;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
