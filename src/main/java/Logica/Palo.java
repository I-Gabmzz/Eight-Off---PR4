package Logica;

// Esta clase es de tipo Enum y define los cuatro palos de la baraja
public enum Palo {
    TREBOL(1, "♣", "negro"),
    DIAMANTE(2, "♦", "rojo"),
    CORAZON(3, "♥", "rojo"),
    PICA(4, "♠", "negro");

    private final int peso; // Valor numerico para comparaciones
    private final String figura; // Simbolo del palo
    private final String color; // Color asociado al palo

    // Metodo constructor del enum
    Palo(int peso, String figura, String color) {
        this.peso = peso;
        this.figura = figura;
        this.color = color;
    }

    // Metodo getter que devuelve el peso del palo
    public int getPeso() {
        return peso;
    }

    // Metodo getter que devuelve el simbolo (figura) del palo
    public String getFigura() {
        return figura;
    }

    // Metodo getter que devuelve el color del palo
    public String getColor() {
        return color;
    }
}