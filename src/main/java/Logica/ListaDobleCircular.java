package Logica;

// Esta clase implementa una estructura de datos de Lista doble Circular
public class ListaDobleCircular<T> {
    private NodoDoble<T> inicio; // Puntero al nodo inicial de la lista
    private NodoDoble<T> fin; // Puntero al nodo final de la lista

    // Metodo constructor que inicializa la lista como vacia
    public ListaDobleCircular() {
        inicio = null;
        fin = null;
    }

    // Metodo que inserta un nuevo nodo al final de la lista circular
    public void insertaFin(T dato) {
        NodoDoble<T> n = new NodoDoble(dato);
        if (fin == null) {
            inicio = fin = n;
            n.setSiguiente(inicio);
            n.setAnterior(inicio);
        } else {
            n.setSiguiente(inicio);
            n.setAnterior(fin);
            fin.setSiguiente(n);
            inicio.setAnterior(n);
            fin = n;
        }
    }

    // Se encarga de eliminar y devolver el dato del nodo que esta al inicio de la lista
    public T eliminaInicio() {
        if (inicio == null) {
            return null;
        }
        T dato = inicio.getInfo();
        if (inicio == fin){
            inicio = fin = null;
            return dato;
        }
        fin.setSiguiente(inicio.getSiguiente());
        inicio.getSiguiente().setAnterior(fin);
        inicio = inicio.getSiguiente();
        return dato;
    }

    // Funcion que recorre la lista y devuelve una representacion en String de sus datos
    public String mostrarLista() {
        String cadena = "";
        NodoDoble<T> r = inicio;
        while (r != fin){
            cadena += r.getInfo() + "\n";
            r = r.getSiguiente();
        }
        cadena += r.getInfo() + "\n";
        return cadena;
    }
}