package practica.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.Noticia;
import practica.pln.AnalizadorSentimiento;

public class AnalizarSentimientoBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    private final AnalizadorSentimiento analizadorSentimiento;

    public AnalizarSentimientoBehaviour(Agent agente) {
        super(agente);
        this.analizadorSentimiento = new AnalizadorSentimiento();
    }

    @Override
    public void action() {
        MessageTemplate plantilla = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage peticion = myAgent.receive(plantilla);

        if (peticion != null) {
            try {
                Noticia noticia = (Noticia) peticion.getContentObject();

                double scoreSentimiento = analizadorSentimiento.analizarSentimiento(noticia.getTextoParaAnalisis());

                ACLMessage respuesta = peticion.createReply();
                respuesta.setPerformative(ACLMessage.INFORM);
                respuesta.setContent(String.valueOf(scoreSentimiento));
                respuesta.setOntology("analisis-sentimiento");
                myAgent.send(respuesta);
                System.out.println("[" + myAgent.getLocalName() + "] -> Análisis de sentimiento completado para: "
                        + noticia.getTitulo() + " (Score: " + scoreSentimiento + ")");

            } catch (Exception e) {
                System.err.println("[" + myAgent.getLocalName() + "] Fallo en comportamiento de sentimiento: "
                        + e.getMessage());
            }
        } else {
            block();
        }
    }
}