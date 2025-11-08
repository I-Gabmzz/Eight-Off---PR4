package Controladores;

import Juego.*;
import Logica.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.stage.Modality;

// Se anuncia la creacion del controlador del tablero que es la clase que controla la logica y la interfaz de usuario del tablero de Eight Off
public class ControladorTablero {
    private EightOffGame juego; // Instancia principal del juego que maneja la logica
    private boolean undoEnProgreso = false; // Bandera para evitar interrupciones mientras se deshace un movimiento
    private MovimientoPosible pistaActual = null; // Almacena el movimiento de pista sugerido actualmente

    // Parametros de la carta
    private static final double ANCHO_CARTA = 150; // Ancho visual de la carta en pixeles
    private static final double ALTO_CARTA = 200; // Alto visual de la carta en pixeles
    private static final double RADIO_ESQUINA_CARTA = 10.0; // Radio de las esquinas redondeadas de la carta
    private static final double ESPACIO_VISIBLE_SOLAPADO = 30; // Espacio visible entre cartas apiladas en el tablero

    // Estilos para las selecciones
    private static final String ESTILO_SLOT_NORMAL = "-fx-background-color: rgba(40, 40, 40, 0.5); -fx-background-radius: 10; -fx-border-color: white; -fx-border-style: solid; -fx-border-width: 1; -fx-border-radius: 10;";
    private static final String ESTILO_CARTA_VISUAL_BASE = "-fx-border-color: black; -fx-border-width: 1; -fx-border-radius: "+RADIO_ESQUINA_CARTA+"; -fx-background-radius: "+RADIO_ESQUINA_CARTA+";";
    private static final String ESTILO_SELECCIONADO = ESTILO_CARTA_VISUAL_BASE + " -fx-effect: dropshadow(gaussian, rgba(255,215,0,0.9), 15, 0.5, 0, 0);";
    private static final String ESTILO_PISTA_ORIGEN = ESTILO_CARTA_VISUAL_BASE + " -fx-effect: dropshadow(gaussian, rgba(255,109,0,0.9), 20, 0.6, 0, 0);";
    private static final String ESTILO_PISTA_DESTINO_CARTA = ESTILO_CARTA_VISUAL_BASE + " -fx-effect: dropshadow(gaussian,  rgba(255,109,0,0.9), 20, 0.6, 0, 0);";
    private static final String ESTILO_PISTA_DESTINO_SLOT = ESTILO_SLOT_NORMAL + " -fx-effect: dropshadow(gaussian,  rgba(255,109,0,0.9), 20, 0.6, 0, 0);";

    // Atributos de la seleccion para las pistas
    private enum Seleccion { NADA, RESERVA, TABLERO } // Enumerador para el tipo de seleccion actual
    private Seleccion seleccionActual = Seleccion.NADA; // Almacena el tipo de la seleccion actual
    private int indiceSeleccionado = -1; // Almacena el indice del slot/columna seleccionado
    private int cantidadSeleccionada = 1; // Almacena la cantidad de cartas seleccionadas (para escaleras)

    // Elementos del tablero
    @FXML private StackPane reserva0, reserva1, reserva2, reserva3, reserva4, reserva5, reserva6, reserva7; // Slots de la reserva (celdas libres)
    @FXML private StackPane fundacion0, fundacion1, fundacion2, fundacion3; // Slots de las fundaciones (pilas de palo)
    @FXML private VBox tableroColumna0, tableroColumna1, tableroColumna2, tableroColumna3, tableroColumna4, tableroColumna5, tableroColumna6, tableroColumna7; // VBox para cada una de las 8 columnas del tablero
    @FXML private Button botonPista; // Boton de control para pedir una pista
    @FXML private Button botonMenu; // Boton de control para regresar al menu
    @FXML private Button botonUndo; // Boton de control para deshacer un movimiento
    @FXML private Button botonHistorial; // Boton de control para abrir el historial
    private StackPane[] reservasSlots; // Arreglo para acceder facilmente a los slots de reserva
    private StackPane[] fundacionesSlots; // Arreglo para acceder facilmente a los slots de fundacion
    private VBox[] columnasTableroVBoxes; // Arreglo para acceder facilmente a las VBox del tablero
    private StackPane[] tableroSlotsBase; // Arreglo para los slots base (espacios vacios) de cada columna

    // Metodo que se ejecuta al iniciar la ventana FXML
    // Se encarga de agrupar los elementos de la UI en arreglos, configurar los botones e iniciar el juego
    @FXML
    public void initialize() {
        reservasSlots = new StackPane[]{reserva0, reserva1, reserva2, reserva3, reserva4, reserva5, reserva6, reserva7};
        fundacionesSlots = new StackPane[]{fundacion0, fundacion1, fundacion2, fundacion3};
        columnasTableroVBoxes = new VBox[]{tableroColumna0, tableroColumna1, tableroColumna2, tableroColumna3, tableroColumna4, tableroColumna5, tableroColumna6, tableroColumna7};

        tableroSlotsBase = new StackPane[columnasTableroVBoxes.length];
        for(int i=0; i<columnasTableroVBoxes.length; i++) {
            if (!columnasTableroVBoxes[i].getChildren().isEmpty() && columnasTableroVBoxes[i].getChildren().get(0) instanceof StackPane) {
                tableroSlotsBase[i] = (StackPane) columnasTableroVBoxes[i].getChildren().get(0);
            } else {
                tableroSlotsBase[i] = new StackPane();
                tableroSlotsBase[i].setPrefSize(ANCHO_CARTA, ALTO_CARTA);
                tableroSlotsBase[i].setMinSize(ANCHO_CARTA, ALTO_CARTA);
                tableroSlotsBase[i].setStyle(ESTILO_SLOT_NORMAL);
                columnasTableroVBoxes[i].getChildren().add(0, tableroSlotsBase[i]);
            }
        }
        botonUndo.setOnAction(e -> controladorDeUndo());
        botonMenu.setOnAction(e -> { try { controladorDeMenu(); } catch (IOException ex) { ex.printStackTrace(); } });
        botonPista.setOnAction(e -> controladorDePista());
        botonHistorial.setOnAction(e -> { try { controladorDeHistorial(); } catch (IOException ex) { ex.printStackTrace(); } });
        iniciarNuevoJuego();
    }

    // Funcion que crea una nueva instancia del juego y reinicia la interfaz grafica
    private void iniciarNuevoJuego() {
        ControlDeMovimientos.resetHistorial();
        juego = new EightOffGame();
        limpiarSeleccion();
        dibujar();
    }

    // Metodo principal que actualiza toda la interfaz grafica
    // Llama a las funciones de dibujar reservas, fundaciones y tablero
    public void dibujar() {
        limpiarTodasLasCartasVista();
        dibujarReservas();
        dibujarFoundations();
        dibujarTablero();
        actualizarBotonUndo();
        if (juegoGanado()) {
            mostrarVictoriaYVolverMenu();
        }
    }

    // Se encarga de borrar todas las cartas visuales de la interfaz antes de redibujar
    private void limpiarTodasLasCartasVista() {
        for (StackPane slot : reservasSlots) {
            slot.getChildren().removeIf(node -> node.getUserData() instanceof Carta);
            slot.setOnMouseClicked( e -> clickEnReserva(getIndex(reservasSlots, slot)) );
        }
        for (StackPane slot : fundacionesSlots) {
            slot.getChildren().removeIf(node -> node.getUserData() instanceof Carta);
            slot.setOnMouseClicked( e -> clickEnFoundation(getIndex(fundacionesSlots, slot)) );
        }
        for (int i = 0; i < columnasTableroVBoxes.length; i++) {
            VBox columna = columnasTableroVBoxes[i];
            if (columna.getChildren().size() > 1) {
                columna.getChildren().remove(1, columna.getChildren().size());
            }
            final int index = i;
            if (!columna.getChildren().isEmpty()) {
                columna.getChildren().get(0).setOnMouseClicked(e -> clickEnTablearo(index, -1)); // Clic en base
            }
        }
    }

    // Metodo de utilidad para encontrar el indice de un StackPane en un arreglo
    private int getIndex(StackPane[] vector, StackPane elemento) {
        for (int i = 0; i < vector.length; i++) { if (vector[i] == elemento) return i; } return -1;
    }

    // Funcion que dibuja las cartas en las 8 celdas de reserva
    private void dibujarReservas() {
        for (int i = 0; i < reservasSlots.length; i++) {
            StackPane slotReserva = reservasSlots[i];
            Carta cartaLogica = juego.getCeldaReserva(i).getCarta();
            boolean esDestinoPistaReserva = pistaActual != null && pistaActual.destinoTipo == MovimientoPosible.TIPO_DESTINO_RESERVA && pistaActual.destinoIndice == i;
            boolean esOrigenPistaReserva = pistaActual != null && pistaActual.origenTipo == MovimientoPosible.TIPO_ORIGEN_RESERVA && pistaActual.origenIndice == i;

            if (cartaLogica != null) {
                slotReserva.setStyle(ESTILO_SLOT_NORMAL);
                StackPane nodoCarta = crearNodoCarta(cartaLogica);
                nodoCarta.setUserData(cartaLogica);
                if (esOrigenPistaReserva) {
                    nodoCarta.setStyle(ESTILO_PISTA_ORIGEN);
                } else if (seleccionActual == Seleccion.RESERVA && indiceSeleccionado == i) {
                    nodoCarta.setStyle(ESTILO_SELECCIONADO);
                } else {
                    nodoCarta.setStyle(ESTILO_CARTA_VISUAL_BASE);
                }
                final int indiceActual = i;
                nodoCarta.setOnMouseClicked(e -> clickEnReserva(indiceActual));
                slotReserva.getChildren().add(nodoCarta);
                slotReserva.setOnMouseClicked(null);
            } else {
                slotReserva.setStyle(esDestinoPistaReserva ? ESTILO_PISTA_DESTINO_SLOT : ESTILO_SLOT_NORMAL);
                slotReserva.setOnMouseClicked(e -> clickEnReserva(getIndex(reservasSlots, slotReserva)));
            }
        }
    }

    // Se encarga de dibujar la carta superior de cada una de las 4 fundaciones
    private void dibujarFoundations() {
        for (int i = 0; i < fundacionesSlots.length; i++) {
            StackPane slotFundacion = fundacionesSlots[i];
            Carta cartaLogica = juego.getPilaFundacion(i).getCartaSuperior();

            boolean esDestinoPistaF = pistaActual != null && pistaActual.destinoTipo == MovimientoPosible.TIPO_DESTINO_FUNDACION && pistaActual.destinoIndice == i;

            if (cartaLogica != null) {
                slotFundacion.setStyle(ESTILO_SLOT_NORMAL);
                StackPane nodoCarta = crearNodoCarta(cartaLogica);
                nodoCarta.setUserData(cartaLogica);
                nodoCarta.setStyle(esDestinoPistaF ? ESTILO_PISTA_DESTINO_CARTA : ESTILO_CARTA_VISUAL_BASE);
                if (slotFundacion.getChildren().size() > 0 && slotFundacion.getChildren().get(0) instanceof Label) {
                    slotFundacion.getChildren().add(nodoCarta);
                } else {
                    slotFundacion.getChildren().add(0, nodoCarta);
                }
                slotFundacion.setOnMouseClicked(null);
                final int indiceActual = i;
                nodoCarta.setOnMouseClicked(e -> clickEnFoundation(indiceActual));
            } else {
                slotFundacion.setStyle(esDestinoPistaF ? ESTILO_PISTA_DESTINO_SLOT : ESTILO_SLOT_NORMAL);
                slotFundacion.setOnMouseClicked(e -> clickEnFoundation(getIndex(fundacionesSlots, slotFundacion)));
            }
        }
    }

    // Metodo que dibuja todas las cartas apiladas en las 8 columnas del tablero
    private void dibujarTablero() {
        for (int i = 0; i < columnasTableroVBoxes.length; i++) {
            VBox columnaVBox = columnasTableroVBoxes[i];
            StackPane slotBase = tableroSlotsBase[i];
            if (columnaVBox.getChildren().size() > 1) {
                columnaVBox.getChildren().remove(1, columnaVBox.getChildren().size());
            }
            List<Carta> cartasLogica = juego.getPilaTablero(i).getCartasParaVista(); // Fondo -> Cima
            boolean columnaVacia = cartasLogica.isEmpty();
            slotBase.setVisible(columnaVacia);
            slotBase.setManaged(columnaVacia);
            boolean estaColumnaSeleccionada = (seleccionActual == Seleccion.TABLERO && indiceSeleccionado == i);
            boolean esOrigenPistaColumna  = (pistaActual != null && pistaActual.origenTipo == MovimientoPosible.TIPO_ORIGEN_TABLERO && pistaActual.origenIndice == i);
            boolean esDestinoPistaColumna = (pistaActual != null && pistaActual.destinoTipo == MovimientoPosible.TIPO_DESTINO_TABLERO && pistaActual.destinoIndice == i);
            if (pistaActual != null && pistaActual.destinoTipo == MovimientoPosible.TIPO_DESTINO_TABLERO && pistaActual.destinoIndice == i) {
                slotBase.setStyle(ESTILO_PISTA_DESTINO_SLOT);
            }
            for (int j = 0; j < cartasLogica.size(); j++) {
                Carta carta = cartasLogica.get(j);
                StackPane nodoCarta = crearNodoCarta(carta);
                nodoCarta.setUserData(carta);
                double parteOculta = ALTO_CARTA - ESPACIO_VISIBLE_SOLAPADO;
                double posY = j * -parteOculta;
                nodoCarta.setTranslateY(posY);
                boolean esParteDeEscaleraSeleccionada = false;
                if (estaColumnaSeleccionada) {
                    int inicioEscaleraLogico = cartasLogica.size() - cantidadSeleccionada;
                    if (j >= inicioEscaleraLogico) { esParteDeEscaleraSeleccionada = true; }
                }
                boolean esParteDeOrigenPista = false;
                if (esOrigenPistaColumna) {
                    int cantidadPista = pistaActual.cantidad;
                    int inicioEscaleraPista = cartasLogica.size() - cantidadPista;
                    if (j >= inicioEscaleraPista) { esParteDeOrigenPista = true; }
                }
                boolean esDestinoPistaSuperior = (esDestinoPistaColumna && j == cartasLogica.size() - 1);
                if (esParteDeOrigenPista) {
                    nodoCarta.setStyle(ESTILO_PISTA_ORIGEN);
                } else if (esDestinoPistaSuperior) {
                    nodoCarta.setStyle(ESTILO_PISTA_DESTINO_CARTA);
                } else if (esParteDeEscaleraSeleccionada) {
                    nodoCarta.setStyle(ESTILO_SELECCIONADO);
                } else {
                    nodoCarta.setStyle(ESTILO_CARTA_VISUAL_BASE);
                }
                int indiceColumnaActual = i;
                int indiceCartaEnColumnaActual = j;
                nodoCarta.setOnMouseClicked(e -> clickEnTablearo(indiceColumnaActual, indiceCartaEnColumnaActual));
                columnaVBox.getChildren().add(nodoCarta);
            }
            final int indexCol = i;
            if (columnaVacia) {
                slotBase.setStyle(ESTILO_SLOT_NORMAL);
                if (pistaActual != null && pistaActual.destinoTipo == 0 && pistaActual.destinoIndice == i) {
                    slotBase.setStyle(ESTILO_PISTA_DESTINO_SLOT);
                }
                slotBase.setOnMouseClicked(e -> clickEnTablearo(indexCol, -1));
            } else {
                slotBase.setOnMouseClicked(null);
            }
        }
    }

    // Funcion que genera un StackPane visual para una carta logica
    private StackPane crearNodoCarta(Carta carta) {
        StackPane nodoCarta = new StackPane();
        nodoCarta.setPrefSize(ANCHO_CARTA, ALTO_CARTA);
        nodoCarta.setMinSize(ANCHO_CARTA, ALTO_CARTA);
        nodoCarta.setMaxSize(ANCHO_CARTA, ALTO_CARTA);
        Rectangle fondo = new Rectangle(ANCHO_CARTA, ALTO_CARTA);
        fondo.setArcWidth(RADIO_ESQUINA_CARTA * 2);
        fondo.setArcHeight(RADIO_ESQUINA_CARTA * 2);
        fondo.setFill(Color.WHITE);
        Label etiquetaEsquina = new Label();
        Label etiquetaCentro = new Label();
        String valorStr = valorACadena(carta.getValor());
        String paloStr = paloAFigura(carta.getPalo());
        Color colorTexto = (carta.getPalo() == Palo.CORAZON || carta.getPalo() == Palo.DIAMANTE) ? Color.RED : Color.BLACK;
        etiquetaEsquina.setText(valorStr + paloStr);
        etiquetaEsquina.setTextFill(colorTexto);
        etiquetaEsquina.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        etiquetaEsquina.setPadding(new Insets(5, 0, 0, 8));
        StackPane.setAlignment(etiquetaEsquina, Pos.TOP_LEFT);
        etiquetaCentro.setText(paloStr);
        etiquetaCentro.setTextFill(colorTexto);
        etiquetaCentro.setFont(Font.font("Arial", FontWeight.NORMAL, 70));
        StackPane.setAlignment(etiquetaCentro, Pos.CENTER);
        nodoCarta.getChildren().addAll(fondo, etiquetaCentro, etiquetaEsquina);
        nodoCarta.setStyle(ESTILO_CARTA_VISUAL_BASE);
        return nodoCarta;
    }

    // Metodo de utilidad para convertir el valor numerico de una carta a String (A, K, Q, J, etc)
    private String valorACadena(int valor) {
        return switch (valor) {
            case 14 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(valor);
        };
    }

    // Metodo de utilidad para obtener el simbolo de un palo
    private String paloAFigura(Palo palo) {
        if (palo == null) return "?";
        return palo.getFigura();
    }

    // Metodo que maneja la logica de clic en una de las celdas de reserva
    private void clickEnReserva(int indiceReserva) {
        if (undoEnProgreso) return;
        Carta cartaEnReserva = juego.getCeldaReserva(indiceReserva).getCarta();
        switch (seleccionActual) {
            case NADA:
                if (cartaEnReserva != null) {
                    pistaActual = null;
                    seleccionActual = Seleccion.RESERVA;
                    indiceSeleccionado = indiceReserva;
                    dibujar();
                }
                break;
            case RESERVA:
                if (indiceSeleccionado == indiceReserva) {
                    int indiceF = encontrarIndiceFundacion(cartaEnReserva.getPalo());
                    if (indiceF != -1 && juego.moverDeReservaAFundacion(indiceReserva, indiceF)) {
                        limpiarSeleccion();
                    } else {
                        limpiarSeleccion();
                    }
                } else {
                    if (cartaEnReserva != null) {
                        indiceSeleccionado = indiceReserva;
                        dibujar();
                    } else {
                        limpiarSeleccion();
                    }
                }
                break;
            case TABLERO:
                if (cartaEnReserva == null) {
                    if (juego.moverDeTableroAReserva(indiceSeleccionado, indiceReserva)) {
                        limpiarSeleccion();
                    } else {
                        limpiarSeleccion();
                    }
                } else {
                    limpiarSeleccion();
                }
                break;
        }
    }

    // Metodo que maneja la logica de clic en una de las pilas de fundacion
    private void clickEnFoundation(int indiceFundacion) {
        if (undoEnProgreso) return;

        switch (seleccionActual) {
            case RESERVA:
                Carta cartaSeleccionadaR = juego.getCeldaReserva(indiceSeleccionado).getCarta();
                if (cartaSeleccionadaR != null) {
                    if (juego.getPilaFundacion(indiceFundacion).sePuedeAgregar(cartaSeleccionadaR)) {
                        if(juego.moverDeReservaAFundacion(indiceSeleccionado, indiceFundacion)) {
                            limpiarSeleccion();
                        } else {
                            limpiarSeleccion();
                        }
                    } else {
                        limpiarSeleccion();
                    }
                } else {
                    limpiarSeleccion();
                }
                break;
            case TABLERO:
                Carta cartaSeleccionadaT = juego.getPilaTablero(indiceSeleccionado).getCartaSuperior();
                if (cartaSeleccionadaT != null) {
                    if (juego.getPilaFundacion(indiceFundacion).sePuedeAgregar(cartaSeleccionadaT)) {
                        if (juego.moverDeTableroAFundacion(indiceSeleccionado, indiceFundacion)) {
                            limpiarSeleccion();
                        } else {
                            limpiarSeleccion();
                        }
                    } else {
                        limpiarSeleccion();
                    }
                } else {
                    limpiarSeleccion();
                }
                break;
            case NADA:
                limpiarSeleccion();
                break;
        }
    }

    // Metodo que maneja la logica de clic en una carta o slot del tablero
    private void clickEnTablearo(int indiceColumna, int indiceCartaEnColumna) {
        if (undoEnProgreso) return;
        List<Carta> cartasColumnaActual = juego.getPilaTablero(indiceColumna).getCartasParaVista();
        boolean esClicEnBaseVacia = (indiceCartaEnColumna == -1);
        switch (seleccionActual) {
            case NADA:
                if (!esClicEnBaseVacia) {
                    pistaActual = null;
                    int cantidadMovible = calcularCantidadEscaleraDesde(cartasColumnaActual, indiceCartaEnColumna);
                    if (cantidadMovible > 0) {
                        seleccionActual = Seleccion.TABLERO;
                        indiceSeleccionado = indiceColumna;
                        cantidadSeleccionada = cantidadMovible;
                        dibujar();
                    }
                }
                break;

            case RESERVA:
                if (juego.moverDeReservaATablero(indiceSeleccionado, indiceColumna)) {
                    limpiarSeleccion();
                } else {
                    limpiarSeleccion();
                }
                break;

            case TABLERO:
                if (indiceSeleccionado == indiceColumna) {
                    boolean esClicEnCartaSuperiorActual = (indiceCartaEnColumna == cartasColumnaActual.size() - 1);
                    if (esClicEnCartaSuperiorActual && cantidadSeleccionada == 1) {
                        Carta cartaSuperior = juego.getPilaTablero(indiceColumna).getCartaSuperior();
                        if (cartaSuperior != null) {
                            int indiceF = encontrarIndiceFundacion(cartaSuperior.getPalo());
                            if (indiceF != -1 && juego.moverDeTableroAFundacion(indiceColumna, indiceF)) {
                                limpiarSeleccion();
                                return;
                            }
                        }
                    }
                    limpiarSeleccion();

                } else {
                    if (juego.moverEscaleraDeTableroATablero(indiceSeleccionado, indiceColumna, cantidadSeleccionada)) {
                        limpiarSeleccion();
                    } else {
                        limpiarSeleccion();
                    }
                }
                break;
        }
    }

    // Metodo para reiniciar la seleccion del usuario
    private void limpiarSeleccion() { limpiarSeleccion(true); }

    // Metodo que reinicia la seleccion, con opcion de mantener la pista actual
    private void limpiarSeleccion(boolean borrarPista) {
        seleccionActual = Seleccion.NADA;
        indiceSeleccionado = -1;
        cantidadSeleccionada = 1;
        if (borrarPista) {
            pistaActual = null;
        }
        dibujar();
    }

    // Metodo que maneja el evento del boton Undo o deshacer
    @FXML
    private void controladorDeUndo() {
        if (undoEnProgreso) return;

        undoEnProgreso = true;
        botonUndo.setDisable(true);

        try {
            boolean deshecho = ControlDeMovimientos.deshacer(
                    juego.getPilasDeTablero(),
                    juego.getCeldasDeReserva(),
                    juego.getPilasDeFundacion()
            );

            if (deshecho) {
                limpiarSeleccion();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            undoEnProgreso = false;
            actualizarBotonUndo();
        }
    }

    // Metodo que maneja el evento del boton Menu, regresa a la pantalla inicial
    @FXML
    private void controladorDeMenu() throws IOException {
        Parent raizMenu = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/InterfazInicial.fxml")));
        Stage ventanaActual = (Stage) botonMenu.getScene().getWindow();
        Scene escenaMenu = new Scene(raizMenu, ventanaActual.getScene().getWidth(), ventanaActual.getScene().getHeight());
        ventanaActual.setScene(escenaMenu);
        ventanaActual.setTitle("Menu Principal");
        ventanaActual.show();
    }

    // Metodo que maneja el evento del boton Pista, busca y resalta un movimiento
    @FXML
    private void controladorDePista() {
        limpiarSeleccion(false);
        pistaActual = juego.buscarPista();
        if (pistaActual != null) {
            dibujar();
            return;
        }
        pistaActual = null;
        if (!hayEspacioEnReservas()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sin Movimientos");
            alert.setHeaderText("No hay movimientos posibles y las reservas estan llenas.");
            alert.setContentText("¿Regresar al menu?");
            alert.getButtonTypes().setAll(
                    javafx.scene.control.ButtonType.YES,
                    javafx.scene.control.ButtonType.NO
            );
            var res = alert.showAndWait();
            if (res.isPresent() && res.get() == javafx.scene.control.ButtonType.YES) {
                try { controladorDeMenu(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    // Se encarga de habilitar o deshabilitar el boton Undo segun el historial
    private void actualizarBotonUndo() {
        boolean puede = ControlDeMovimientos.puedeDeshacer();
        botonUndo.setDisable(!puede);
    }

    // Metodo que maneja el evento del boton Historial
    @FXML
    private void controladorDeHistorial() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Historial.fxml"));
        Parent root = loader.load();

        ControladorHistorial controladorHistorial = loader.getController();

        controladorHistorial.initData(this, juego);

        Stage stage = new Stage();
        stage.setTitle("Historial de Movimientos");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(botonHistorial.getScene().getWindow());

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.showAndWait();
        dibujar();
    }

    // Funcion que busca el indice de la fundacion correcta para un palo especifico
    private int encontrarIndiceFundacion(Palo paloBuscado) {
        if (paloBuscado == null) return -1;
        for (int i = 0; i < juego.getPilasDeFundacion().length; i++) {
            Foundation f = juego.getPilaFundacion(i);
            if (!f.estaVacia() && f.getPalo() == paloBuscado) {
                return i;
            }
        }
        for (int i = 0; i < juego.getPilasDeFundacion().length; i++) {
            if (juego.getPilaFundacion(i).estaVacia()) {
                return i;
            }
        }
        return -1;
    }

    // Metodo que calcula cuantas cartas forman una escalera valida desde el punto de clic
    private int calcularCantidadEscaleraDesde(List<Carta> cartasColumna, int indiceClicado) {
        if (cartasColumna == null || indiceClicado < 0 || indiceClicado >= cartasColumna.size()) {
            return 0;
        }
        Carta cartaBase = cartasColumna.get(indiceClicado);
        Palo paloEscalera = cartaBase.getPalo();
        int valorPrevio = cartaBase.getValor();
        int cantidad = 1;
        for (int i = indiceClicado + 1; i < cartasColumna.size(); i++) {
            Carta cartaActual = cartasColumna.get(i);
            if (cartaActual.getPalo() == paloEscalera && cartaActual.getValor() == valorPrevio - 1) {
                cantidad++;
                valorPrevio = cartaActual.getValor();
            } else {
                return 0;
            }
        }
        return cantidad;
    }

    // Funcion que comprueba si queda al menos un espacio libre en la reserva
    private boolean hayEspacioEnReservas() {
        for (int i = 0; i < reservasSlots.length; i++) {
            if (juego.getCeldaReserva(i).getCarta() == null) return true;
        }
        return false;
    }

    // Metodo que comprueba si se cumplen las condiciones de victoria
    private boolean juegoGanado() {
        for (int i = 0; i < reservasSlots.length; i++) {
            if (juego.getCeldaReserva(i).getCarta() != null) {
                return false;
            }
        }
        for (int i = 0; i < juego.getPilasDeTablero().length; i++) {
            if (!juego.getPilaTablero(i).estaVacia()) {
                return false;
            }
        }
        return true;
    }

    // Se encarga de mostrar una alerta de victoria y regresar al menu
    private void mostrarVictoriaYVolverMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoria");
        alert.setHeaderText("Has ganado la partida");
        alert.setContentText("Todas las cartas fueron colocadas exitosamente.\n\nVolveras al menu principal.");
        alert.showAndWait();
        try {
            controladorDeMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}