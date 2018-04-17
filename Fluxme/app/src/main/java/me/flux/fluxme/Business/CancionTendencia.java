package me.flux.fluxme.Business;

/**
 * Created by JosueAndroid on 16/4/2018.
 */

public class CancionTendencia {
    private int posicion;
    private String cancion;
    private String artista;
    private String link;

    public CancionTendencia(int posicion, String cancion, String artista, String link) {
        this.posicion = posicion;
        this.cancion = cancion;
        this.artista = artista;
        this.link = link;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
