package practica.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.Noticia;

public class RecibirNoticiasBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;

    public RecibirNoticiasBehaviour(Agent agente) {
        super(agente);
    }

    @Override
    public void action() {
        MessageTemplate plantilla = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage mensaje = myAgent.receive(plantilla);

        if (mensaje != null) {
            try {
                Object contenido = mensaje.getContentObject();
                if (contenido instanceof Noticia) {
                    Noticia noticia = (Noticia) contenido;
                    System.out.println("[" + myAgent.getLocalName() + "] -> ComportamientoRecibirNoticias: "
                            + "Noticia capturada con éxito: " + noticia.getTitulo());
                }
            } catch (Exception e) {
                System.err.println("[" + myAgent.getLocalName() + "] Error al deserializar objeto Noticia: " 
                        + e.getMessage());
            }
        } else {
            block();
        }
    }
}