package practica.behaviour;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.InformeCredibilidad;

public class AgregarResultadosBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    private static final String SERVICIO_SENTIMIENTO = "analisis-sentimiento";
    private static final String SERVICIO_SESGO = "deteccion-sesgo";
    private static final String SERVICIO_FUENTE = "verificacion-fuente";
    private static final int TOTAL_RESPUESTAS_ESPERADAS = 3;

    private final Map<String, InformeCredibilidad> informesPendientes = new ConcurrentHashMap<>(); // informe parcial
                                                                                                   // acumulado
    private final Map<String, Integer> contadorRespuestas = new ConcurrentHashMap<>(); // numero de respuestas recibidas

    public AgregarResultadosBehaviour(Agent agente) {
        super(agente);
    }

    @Override
    public void action() {
        // acepta respuestas INFORM de los expertos que tengan conversation-id
        MessageTemplate plantilla = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage respuesta = myAgent.receive(plantilla);

        if (respuesta != null) {
            String idConversacion = respuesta.getConversationId();
            String ontologia = respuesta.getOntology();
            if (idConversacion == null || ontologia == null) {
                return;
            }
            InformeCredibilidad informe = informesPendientes.computeIfAbsent(
                    idConversacion, k -> new InformeCredibilidad());

            double score;
            try {
                score = Double.parseDouble(respuesta.getContent());
            } catch (NumberFormatException e) {
                System.err.println("[" + myAgent.getLocalName() + "] Score no numérico recibido de "
                        + respuesta.getSender().getLocalName() + ": " + respuesta.getContent());
                return;
            }

            switch (ontologia) {
                case SERVICIO_SENTIMIENTO:
                    informe.setPuntuacionSentimiento(score);
                    informe.addDetalleExperto("Sentimiento",
                            "Puntuación de sentimiento: " + String.format("%.2f", score));
                    System.out.println("[" + myAgent.getLocalName() + "] -> Score sentimiento recibido: " + score
                            + " (conversación: " + idConversacion + ")");
                    break;

                case SERVICIO_SESGO:
                    informe.setPuntuacionSesgo(score);
                    informe.addDetalleExperto("Sesgo", "Puntuación de sesgo: " + String.format("%.2f", score));
                    System.out.println("[" + myAgent.getLocalName() + "] -> Score sesgo recibido: " + score
                            + " (conversación: " + idConversacion + ")");
                    break;

                case SERVICIO_FUENTE:
                    informe.setPuntuacionReputacionFuente(score);
                    informe.addDetalleExperto("Fuente",
                            "Puntuación de reputación de fuente: " + String.format("%.2f", score));
                    System.out.println("[" + myAgent.getLocalName() + "] -> Score reputación fuente recibido: " + score
                            + " (conversación: " + idConversacion + ")");
                    break;

                default:
                    System.err.println("[" + myAgent.getLocalName() + "] Ontología desconocida: " + ontologia);
                    return;
            }

            int totalRecibidas = contadorRespuestas.merge(idConversacion, 1, Integer::sum);

            if (totalRecibidas >= TOTAL_RESPUESTAS_ESPERADAS) {
                informe.calcularPuntuacionFinal();

                System.out.println("========================================");
                System.out.println("[" + myAgent.getLocalName() + "] INFORME DE CREDIBILIDAD COMPLETO");
                System.out.println(informe);
                System.out.println("========================================");

                // Enviamos el informe al visualizador
                ACLMessage informeFinal = new ACLMessage(ACLMessage.INFORM);
                informeFinal.addReceiver(new jade.core.AID("agente-visualizacion", jade.core.AID.ISLOCALNAME));
                try {
                    informeFinal.setContentObject(informe);
                    myAgent.send(informeFinal);
                } catch (java.io.IOException e) {
                    System.err.println("Error enviando informe al visualizador: " + e.getMessage());
                }

                informesPendientes.remove(idConversacion);
                contadorRespuestas.remove(idConversacion);
            }
        } else {
            block();
        }
    }
}
