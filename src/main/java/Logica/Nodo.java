package Logica;

// Se anuncia la creacion de la clase que representa un nodo para una lista simple
public class Nodo<T> {

    private T info; // Almacena el dato del nodo
    private Nodo<T> siguiente; // Puntero al siguiente nodo en la lista

    // Metodo constructor que inicializa el nodo con su informacion
    public Nodo(T info) {
        this.info = info;
    }

    // Metodo getter que devuelve la informacion almacenada en el nodo
    public T getInfo() {
        return info;
    }

    // Metodo getter que devuelve la referencia al siguiente nodo
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    // Se encarga de establecer la referencia al siguiente nodo
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    // Devuelve una representacion en String de la informacion del nodo
    public String toString() {
        return info.toString();
    }
}