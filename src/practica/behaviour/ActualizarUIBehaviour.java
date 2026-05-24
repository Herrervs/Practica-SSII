package practica.behaviour;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.InformeCredibilidad;

public class ActualizarUIBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private JTextArea areaInformes;

    public ActualizarUIBehaviour(Agent agente) {
        super(agente);
        inicializarVentana();
    }

    @Override
    public void action() {
        MessageTemplate plantilla = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchOntology("informe-credibilidad"));
        ACLMessage mensaje = myAgent.receive(plantilla);

        if (mensaje != null) {
            try {
                Object contenido = mensaje.getContentObject();

                if (contenido instanceof InformeCredibilidad) {
                    InformeCredibilidad informe = (InformeCredibilidad) contenido;
                    mostrarInforme(informe);
                } else {
                    System.err.println("[" + myAgent.getLocalName()
                            + "] Contenido no reconocido recibido en visualizacion: " + contenido);
                }
            } catch (Exception e) {
                System.err.println("[" + myAgent.getLocalName()
                        + "] Error al procesar informe de credibilidad: " + e.getMessage());
            }
        } else {
            block();
        }
    }

    private void mostrarInforme(InformeCredibilidad informe) {
        String textoInforme = construirTextoInforme(informe);
        System.out.println("========================================");
        System.out.print(textoInforme);
        System.out.println("========================================");

        if (areaInformes != null) {
            SwingUtilities.invokeLater(() -> {
                areaInformes.append(textoInforme);
                areaInformes.append(System.lineSeparator());
                areaInformes.setCaretPosition(areaInformes.getDocument().getLength());
            });
        }
    }

    private void inicializarVentana() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Informes de credibilidad");
            areaInformes = new JTextArea(24, 80);
            areaInformes.setEditable(false);
            areaInformes.setLineWrap(true);
            areaInformes.setWrapStyleWord(true);

            ventana.setLayout(new BorderLayout());
            ventana.add(new JScrollPane(areaInformes), BorderLayout.CENTER);
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }

    private String construirTextoInforme(InformeCredibilidad informe) {
        StringBuilder salida = new StringBuilder();
        salida.append("[VISUALIZACION] Nuevo informe de credibilidad ")
                .append(LocalDateTime.now().format(FORMATO_HORA))
                .append(System.lineSeparator());
        salida.append("Titulo: ")
                .append(informe.getNoticia() == null ? "desconocido" : informe.getNoticia().getTitulo())
                .append(System.lineSeparator());
        salida.append("Fuente: ")
                .append(informe.getNoticia() == null ? "desconocida" : informe.getNoticia().getFuente())
                .append(System.lineSeparator());
        salida.append("Sentimiento: ").append(String.format("%.2f", informe.getPuntuacionSentimiento()))
                .append(System.lineSeparator());
        salida.append("Sesgo: ").append(String.format("%.2f", informe.getPuntuacionSesgo()))
                .append(System.lineSeparator());
        salida.append("Reputacion fuente: ").append(String.format("%.2f", informe.getPuntuacionReputacionFuente()))
                .append(System.lineSeparator());
        salida.append("Credibilidad final: ").append(String.format("%.2f", informe.getPuntuacionCredibilidadFinal()))
                .append(System.lineSeparator());
        salida.append("Veredicto: ").append(informe.getVeredicto()).append(System.lineSeparator());

        if (!informe.getDetallesExpertos().isEmpty()) {
            salida.append("Detalles expertos:").append(System.lineSeparator());
            informe.getDetallesExpertos().forEach((experto, detalle) ->
                    salida.append(" - ").append(experto).append(": ").append(detalle).append(System.lineSeparator()));
        }

        return salida.toString();
    }
}
