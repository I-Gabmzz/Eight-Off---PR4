package Logica;

import java.util.ArrayList;

// Esta clase implementa una estructura de datos de Lista Doble
public class ListaDoble<T> {
    private NodoDoble<T> inicio; // Puntero al nodo inicial de la lista
    private NodoDoble<T> fin; // Puntero al nodo final de la lista

    // Metodo constructor que inicializa la lista como vacia
    public ListaDoble() {
        inicio = null;
        fin = null;
    }

    // Funcion que comprueba si la lista esta vacia
    public boolean estaVacia() {
        return inicio == null;
    }

    // Metodo que inserta un nuevo nodo al inicio de la lista
    public void insertarInicio(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        nodo.setSiguiente(inicio);
        nodo.setAnterior(null);

        if (inicio != null) {
            inicio.setAnterior(nodo);
        } else {
            fin = nodo;
        }

        inicio = nodo;
    }

    // Metodo que inserta un nuevo nodo al final de la lista
    public void insertarFin(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        nodo.setSiguiente(null);
        if (inicio == null){
            nodo.setAnterior(null);
            inicio = nodo;
            fin = nodo;
        } else {
            fin.setSiguiente(nodo);
            nodo.setAnterior(fin);
            fin = nodo;
        }
    }

    // Se encarga de eliminar y devolver el dato del nodo que esta al inicio
    public T eliminarInicio() {
        if (inicio == null) {
            return null;
        } else {
            T dato = inicio.getInfo();
            if (inicio.getSiguiente() == null) {
                inicio = null;
                fin = null;
            } else {
                inicio = inicio.getSiguiente();
                inicio.setAnterior(null);
            }
            return dato;
        }
    }

    // Se encarga de eliminar y devolver el dato del nodo que esta al final
    public T eliminarFin() {
        if (inicio == null) {
            return null;
        } else {
            T dato;
            if (inicio.getSiguiente() == null) {
                dato = inicio.getInfo();
                inicio = null;
                fin = null;
            } else {
                NodoDoble<T> r = fin;
                dato = r.getInfo();
                fin = r.getAnterior();
                fin.setSiguiente(null);
            }
            return dato;
        }
    }

    // Metodo getter que devuelve el nodo de inicio de la lista
    public NodoDoble<T> getInicio() {
        return inicio;
    }

    // Metodo getter que devuelve el nodo final de la lista
    public NodoDoble<T> getFin() {
        return fin;
    }

    // Borra todos los nodos del nodo dado
    public void truncarDesde(NodoDoble<T> nodo) {
        if (nodo == null) {
            inicio = null;
            fin = null;
        } else {
            nodo.setSiguiente(null);
            fin = nodo;
        }
    }

    // Devuelve una lista de todos los nodos (para el visor del historial)
    public ArrayList<NodoDoble<T>> getNodos() {
        ArrayList<NodoDoble<T>> lista = new ArrayList<>();
        NodoDoble<T> r = inicio;
        while (r != null) {
            lista.add(r);
            r = r.getSiguiente();
        }
        return lista;
    }
}