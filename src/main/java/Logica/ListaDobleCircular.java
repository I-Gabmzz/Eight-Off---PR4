package Logica;

public class ListaDobleCircular<T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;


    public ListaDobleCircular() {
        inicio = null;
        fin = null;
    }


    public void insertaInicio(T dato) {
        NodoDoble<T> n = new NodoDoble(dato);
        if (inicio == null) {
            inicio = fin = n;
            n.setSiguiente(inicio);
            n.setAnterior(inicio);
        } else {
            n.setSiguiente(inicio);
            n.setAnterior(fin);
            fin.setSiguiente(n);
            inicio.setAnterior(n);
            inicio = n;
        }
    }

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

    public T eliminaInicio() {
        if (inicio == null) {
            System.out.println("Lista vacía");
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


    public T eliminaFin() {
        if (inicio == null){
            System.out.println("Lista vacía");
            return null;
        }
        T dato = inicio.getInfo();
        if (inicio == fin){
            inicio = fin = null;
            return dato;
        }
        inicio.setAnterior(fin.getAnterior());
        fin.getAnterior().setSiguiente(inicio);
        fin = fin.getAnterior();
        return dato;
    }


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
