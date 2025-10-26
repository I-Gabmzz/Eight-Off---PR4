package Logica;

// La clase NodoDoble representa un nodo para listas que son dobles o circulares
public class NodoDoble<T> {
    private T info; // Almacena el dato  del nodo
    private NodoDoble<T> siguiente; // Puntero al siguiente nodo en la lista
    private NodoDoble<T> anterior; // Puntero al nodo anterior en la lista

    // Metodo constructor que inicializa el nodo con su informacion
    public NodoDoble(T info) {
        this.info = info;
    }

    // Metodo getter que devuelve la informacion almacenada en el nodo
    public T getInfo() {
        return info;
    }

    // Metodo getter que devuelve la referencia al siguiente nodo
    public NodoDoble<T> getSiguiente() {
        return siguiente;
    }

    // Se encarga de establecer la referencia al siguiente nodo
    public void setSiguiente(NodoDoble<T> siguiente) {
        this.siguiente = siguiente;
    }

    // Se encarga de establecer la referencia al nodo anterior
    public void setAnterior(NodoDoble<T> anterior) {
        this.anterior = anterior;
    }

    // Devuelve una representacion en String de la informacion del nodo
    public String toString() {
        return info.toString();
    }
}
