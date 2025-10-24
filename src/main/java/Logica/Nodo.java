package Logica;

public class Nodo<T> {
    private T info;
    private Nodo<T> siguiente;

    public Nodo(T info) {
        this.info = info;
    }

    public T getInfo() {
        return info;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    public String toString() {
        return info.toString();
    }
}
