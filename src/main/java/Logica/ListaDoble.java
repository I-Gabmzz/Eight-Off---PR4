package Logica;

public class ListaDoble<T> {
    NodoDoble<T> inicio;

    public ListaDoble() {
        inicio = null;
    }

    public void insertarInicio(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        nodo.setSiguiente(inicio);
        nodo.setAnterior(null);

        if (inicio != null) {
            inicio.setAnterior(nodo);
        }

        inicio = nodo;
    }

    public void insertarFin(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        nodo.setSiguiente(null);
        if (inicio == null){
            nodo.setAnterior(inicio);
            inicio = nodo;
        } else {
            NodoDoble<T> r = inicio;

            while (r.getSiguiente()!= null){
                r = r.getSiguiente();
            }

            r.setSiguiente(nodo);
            nodo.setAnterior(r);
        }
    }

    public T eliminarInicio() {
        if (inicio == null) {
            return null;
        } else {
            T dato = inicio.getInfo();
            if (inicio.getSiguiente() == null) {
                inicio = null;

            } else {
                inicio = inicio.getSiguiente();
                inicio.setAnterior(null);
            }

            return dato;
        }
    }

    public T eliminarFin() {
        if (inicio == null) {
            return null;
        } else {
            T dato = inicio.getInfo();;
            if (inicio.getSiguiente() == null) {
                inicio = null;

            } else {
                NodoDoble<T> r = inicio;

                while (r.getSiguiente()!= null) {
                    r = r.getSiguiente();
                }

                dato = r.getInfo();
                r.getAnterior().setSiguiente(null);
            }

            return dato;
        }
    }
}