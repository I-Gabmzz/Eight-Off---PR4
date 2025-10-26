package Logica;

// Clase que implementa una estructura de datos de Lista Simplemente Ligada generica
public class ListaSimple<T> {
    private Nodo<T> inicio; // Apuntador al nodo inicial (cabeza) de la lista

    // Metodo constructor que inicializa la lista como vacia
    public ListaSimple() {
        this.inicio = null;
    }

    // Metodo que inserta un nuevo nodo al inicio de la lista
    public void insertaInicio(T dato){
        Nodo<T> nodo = new Nodo(dato);
        nodo.setSiguiente(inicio);
        inicio = nodo;
    }

    // Se encarga de eliminar y devolver el dato del nodo que esta al inicio de la lista
    public T eliminaInicio(){
        Nodo<T> nodoEliminado = inicio;
        if (inicio == null){
            System.out.println("Lista vacia");
            return null;
        }
        inicio = inicio.getSiguiente();
        return nodoEliminado.getInfo();
    }

    // Funcion que recorre la lista y devuelve una representacion en String de sus datos
    public String mostrarLista() {
        String cadena = "";
        Nodo<T> iter = this.inicio;
        while (iter != null) {
            cadena += iter.getInfo().toString() + "\n";
            iter = iter.getSiguiente();
        }
        return cadena;
    }

    // Metodo que devuelve el dato del nodo inicial sin eliminarlo
    public T verInicio() {
        if (this.inicio == null) {
            return null;
        }
        return this.inicio.getInfo();
    }

    // Metodo getter que devuelve el nodo de inicio de la lista
    public Nodo<T> getInicio() {
        return inicio;
    }
}