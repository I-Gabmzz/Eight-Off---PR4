package Logica;

public class Carta implements Comparable<Carta> {

    private int valor;
    private Palo palo;
    private String color;
    private int valorBajo;
    private boolean bocaArriba;

    public Carta(int valor, Palo palo) {
        this.valor = valor;
        this.palo = palo;
        this.color = palo.obtenerColor();

        if (valor == 14) {
            this.valorBajo = 1;
        } else {
            this.valorBajo = valor;
        }
        this.bocaArriba = false;
    }

    public void ponerBocaAbajo() {
        this.bocaArriba = false;
    }

    public void ponerBocaArriba() {
        this.bocaArriba = true;
    }

    public boolean estaBocaArriba() {
        return this.bocaArriba;
    }


    @Override
    public int compareTo(Carta otraCarta) {
        if (this.obtenerValor() != otraCarta.obtenerValor()) {
            return this.obtenerValor() - otraCarta.obtenerValor();
        }

        return this.palo.obtenerPeso() - otraCarta.obtenerPalo().obtenerPeso();
    }

    public boolean esColorOpuesto(Carta otraCarta) {
        return !this.color.equals(otraCarta.obtenerColor());
    }

    public boolean esAnteriorEnRango(Carta otraCarta) {
        return (this.valor + 1) == otraCarta.obtenerValor();
    }

    public boolean esSiguienteEnRango(Carta otraCarta) {
        return (this.valor - 1) == otraCarta.obtenerValor();
    }

    public boolean esAs() {
        return this.valor == 14;
    }

    public boolean esRey() {
        return this.valor == 13;
    }


    public int obtenerValor() {
        return valor;
    }

    public Palo obtenerPalo() {
        return palo;
    }

    public String obtenerColor() {
        return color;
    }

    public int obtenerValorBajo() {
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
        return valorComoCadena + palo.obtenerFigura();
    }
}