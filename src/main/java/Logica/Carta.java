package Logica;

// Esta clase representa una carta individual de la baraja
public class Carta implements Comparable<Carta> {
    private int valor; // Valor numerico de la carta
    private Palo palo; // Palo de la carta
    private String color; // Color de la carta (Rojo o Negro)
    private int valorBajo; // Valor especifico para el As, para los demas es igual al valor
    private boolean bocaArriba; // Bandera que indica si la carta esta visible o no

    // Metodo constructor que inicializa la carta con su valor y palo
    public Carta(int valor, Palo palo) {
        this.valor = valor;
        this.palo = palo;
        this.color = palo.getColor();
        // Asigna el valor bajo (1 para As, el resto normal)
        if (valor == 14) {
            this.valorBajo = 1;
        } else {
            this.valorBajo = valor;
        }
        this.bocaArriba = false;
    }

    // Metodo que cambia el estado de la carta para hacerla visible
    public void ponerBocaArriba() {
        this.bocaArriba = true;
    }

    // Metodo que compara esta carta con otra basado en valor y luego en palo
    @Override
    public int compareTo(Carta otraCarta) {
        if (this.getValor() != otraCarta.getValor()) {
            return this.getValor() - otraCarta.getValor();
        }

        return this.palo.getPeso() - otraCarta.getPalo().getPeso();
    }

    // Funcion que comprueba si otra carta es exactamente un valor menor que esta
    public boolean esSiguienteEnRango(Carta otraCarta) {
        return (this.valor - 1) == otraCarta.getValor();
    }

    // Metodo getter que devuelve el valor principal de la carta
    public int getValor() {
        return valor;
    }

    // Metodo getter que devuelve el palo de la carta
    public Palo getPalo() {
        return palo;
    }

    // Metodo getter que devuelve el valor bajo
    public int getValorBajo() {
        return valorBajo;
    }

    @Override
    public String toString() {
        if (!bocaArriba) {
            return "null";
        }
        String valorComoCadena;
        switch (valor) {
            case 14:
                valorComoCadena = "A";
                break;
            case 11:
                valorComoCadena = "J";
                break;
            case 12:
                valorComoCadena = "Q";
                break;
            case 13:
                valorComoCadena = "K";
                break;
            default:
                valorComoCadena = String.valueOf(valor);
        }
        return valorComoCadena + palo.getFigura();
    }
}