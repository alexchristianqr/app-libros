package Formularios;

import java.io.Serializable;
import javax.swing.Icon;

public class Libro implements Serializable {

    private String codigo;
    private String nombre;
    private String tipo;
    private String clase;
    private int anio;
    private int numPagina;
    private double costo;
    private Icon portada;

    public Libro() {
    }

    public Libro(String codigo, String nombre, String tipo, String clase, int anio, int numPagina, double costo, Icon portada) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.clase = clase;
        this.anio = anio;
        this.numPagina = numPagina;
        this.costo = costo;
        this.portada = portada;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getNumPagina() {
        return numPagina;
    }

    public void setNumPagina(int numPagina) {
        this.numPagina = numPagina;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Icon getPortada() {
        return portada;
    }

    public void setPortada(Icon portada) {
        this.portada = portada;
    }

}
