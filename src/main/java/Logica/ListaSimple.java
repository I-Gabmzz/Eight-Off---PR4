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



    public void insertaFinal(T dato){
        Nodo<T> nodo = new Nodo(dato);
        // Lista vacía
        if (inicio == null){
            nodo.setSiguiente(inicio);
            inicio = nodo;
        }else{
            Nodo<T> iter = inicio;
            while (iter.getSiguiente() != null){
                iter = iter.getSiguiente();
            }
            iter.setSiguiente(nodo);
            nodo.setSiguiente(null);
        }
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


    public T eliminaFinal(){
        if (inicio == null){
            System.out.println("Lista vacia");
            return null;
        }
        if (inicio.getSiguiente() == null){
            T dato = inicio.getInfo();
            inicio = null;
            return dato;
        }
        Nodo<T> r = inicio;
        Nodo<T> a = r;
        while (r.getSiguiente() != null){
            a=r;
            r = r.getSiguiente();
        }
        a.setSiguiente(null);
        return r.getInfo();
    }


    public String mostrarLista(){
        String cadena = "";
        Nodo<T> iter = inicio;
        while (iter.getSiguiente() != null){
            cadena += iter.getInfo().toString() + "\n";
            iter = iter.getSiguiente();
        }
        cadena += iter.getInfo().toString();
        return cadena;
    }
}
