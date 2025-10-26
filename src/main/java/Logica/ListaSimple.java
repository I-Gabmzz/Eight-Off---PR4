package Logica;

public class ListaSimple<T> {
    private Nodo<T> inicio;

    public ListaSimple() {
        this.inicio = null;
    }

    public void insertaInicio(T dato){
        Nodo<T> nodo = new Nodo(dato);
        nodo.setSiguiente(inicio);
        inicio = nodo;
    }

    public T eliminaInicio(){
        Nodo<T> nodoEliminado = inicio;
        if (inicio == null){
            System.out.println("Lista vacia");
            return null;
        }
        inicio = inicio.getSiguiente();
        return nodoEliminado.getInfo();
    }

    public String mostrarLista() {
        String cadena = "";
        Nodo<T> iter = this.inicio;
        while (iter != null) {
            cadena += iter.getInfo().toString() + "\n";
            iter = iter.getSiguiente();
        }
        return cadena;
    }

    public T verInicio() {
        if (this.inicio == null) {
            return null;
        }
        return this.inicio.getInfo();
    }

    public Nodo<T> getInicio() {
        return inicio;
    }
}
