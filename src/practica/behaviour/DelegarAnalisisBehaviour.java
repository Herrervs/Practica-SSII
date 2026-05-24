package practica.behaviour;

import java.io.IOException;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.Noticia;
import practica.utils.UtilsDF;

// este agente recibe noticias del agente de adquisición y delega el análisis a los agentes expertos (sentimiento, sesgo y verificación de fuente)
// cada peticion lleva el conversation-id y la ontologia para que el comportamiento de agregar resultados pueda correlacionar las respuestas
public class DelegarAnalisisBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    public static final String ONTOLOGIA_NOTICIA_NUEVA = "noticia-nueva";
    private static final String SERVICIO_SENTIMIENTO = "analisis-sentimiento";
    private static final String SERVICIO_SESGO = "deteccion-sesgo";
    private static final String SERVICIO_FUENTE = "verificacion-fuente";

    private final Map<String, Noticia> noticiasPendientes;

    public DelegarAnalisisBehaviour(Agent agente, Map<String, Noticia> noticiasPendientes) {
        super(agente);
        this.noticiasPendientes = noticiasPendientes;
    }

    @Override
    public void action() {
        // solo acepta INFORM (noticias nuevas del agente de adquisición)
        MessageTemplate plantilla = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchOntology(ONTOLOGIA_NOTICIA_NUEVA));
        ACLMessage mensaje = myAgent.receive(plantilla);

        if (mensaje != null) {
            try {
                Object contenido = mensaje.getContentObject();

                if (contenido instanceof Noticia) {
                    Noticia noticia = (Noticia) contenido;
                    System.out.println("[" + myAgent.getLocalName()
                            + "] -> ComportamientoDelegarAnalisis: Noticia recibida: " + noticia.getTitulo());

                    String idConversacion = noticia.getId();
                    noticiasPendientes.put(idConversacion, noticia);

                    delegarAExperto(noticia, SERVICIO_SENTIMIENTO, idConversacion);
                    delegarAExperto(noticia, SERVICIO_SESGO, idConversacion);
                    delegarAExperto(noticia, SERVICIO_FUENTE, idConversacion);
                }
            } catch (Exception e) {
                System.err.println(
                        "[" + myAgent.getLocalName() + "] Error al procesar noticia entrante: " + e.getMessage());
            }
        } else {
            block();
        }
    }

    // busca un agente experto por tipo de servicio en el DF y le envía un REQUEST
    // con la noticia serializada.
    private void delegarAExperto(Noticia noticia, String tipoServicio, String idConversacion) {
        AID experto = UtilsDF.buscarPrimerAgentePorServicio(myAgent, tipoServicio);

        if (experto == null) {
            System.err.println(
                    "[" + myAgent.getLocalName() + "] No se encontró agente para el servicio: " + tipoServicio);
            return;
        }

        ACLMessage peticion = new ACLMessage(ACLMessage.REQUEST);
        peticion.addReceiver(experto);
        peticion.setConversationId(idConversacion);
        peticion.setOntology(tipoServicio);

        try {
            peticion.setContentObject(noticia);
            myAgent.send(peticion);
            System.out.println("[" + myAgent.getLocalName() + "] -> REQUEST enviado a " + experto.getLocalName() + " ("
                    + tipoServicio + ") para: " + noticia.getTitulo());
        } catch (IOException e) {
            System.err.println("[" + myAgent.getLocalName() + "] Error al serializar noticia para " + tipoServicio
                    + ": " + e.getMessage());
        }
    }
}
