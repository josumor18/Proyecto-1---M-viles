package me.flux.fluxme.Business;

/**
 * Created by alvaroramirez on 4/18/18.
 */

public class Programacion {
    private int posicion;
    private String dia;
    private String hora;
    private String titulo;

    public Programacion(String dia, String hora, String titulo){
        //this.posicion = posicion;
        this.dia = dia;
        this.hora = hora;
        this.titulo =titulo;

    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
