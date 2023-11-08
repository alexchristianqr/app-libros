package Formularios;

import java.io.Serializable;
import java.util.ArrayList;

/* Iconos: https://www.flaticon.com/packs/essential-collection?k=1699484506937&log-in=email */

public class ArregloLibros implements Serializable {

    private ArrayList<Libro> listaLibros;

    public ArregloLibros() {
        listaLibros = new ArrayList();
    }

    public void agregarLibro(Libro libroNuevo) {
        listaLibros.add(libroNuevo);
    }

    public Libro obtenerLibro(int i) {
        return listaLibros.get(i);
    }

    public void reemplazarLibro(int i, Libro libroActualizado) {
        listaLibros.set(i, libroActualizado);
    }

    public int totalLibros() {
        return listaLibros.size();
    }

    public void eliminarLibro(String codigo) {
        int posicionLibro = buscarLibro(codigo);
        listaLibros.remove(posicionLibro);
    }

    public void eliminarLibros(/*String codigo*/) {
        for (int i = 0; i < totalLibros(); i++) {
            listaLibros.remove(0);
        }
    }

    public int buscarLibro(String codigo) {
        for (int i = 0; i < totalLibros(); i++) {
            if (codigo.equals(obtenerLibro(i).getCodigo())) {
                return i;
            }
        }
        return -1;
    }
}
