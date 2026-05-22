package practica.comportamiento;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.Noticia;
import practica.pln.DetectorPalabrasClaveSesgo;

public class ComportamientoDetectarSesgo extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    private final DetectorPalabrasClaveSesgo analistaSesgo;

    public ComportamientoDetectarSesgo(Agent agente) {
        super(agente);
        this.analistaSesgo = new DetectorPalabrasClaveSesgo();
    }

    @Override
    public void action() {
        MessageTemplate plantilla = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage peticion = myAgent.receive(plantilla);

        if (peticion != null) {
            try {
                Noticia noticia = (Noticia) peticion.getContentObject();
                
                double scoreSesgo = analistaSesgo.detectarSesgo(noticia.getTextoParaAnalisis());

                ACLMessage respuesta = peticion.createReply();
                respuesta.setPerformative(ACLMessage.INFORM);
                respuesta.setContent(String.valueOf(scoreSesgo));
                respuesta.setOntology("deteccion-sesgo");
                myAgent.send(respuesta);
                System.out.println("[" + myAgent.getLocalName() + "] -> Análisis de sesgo completado para: " 
                        + noticia.getTitulo() + " (Score: " + scoreSesgo + ")");

            } catch (Exception e) {
                System.err.println("[" + myAgent.getLocalName() + "] Fallo en comportamiento de sesgo: " 
                        + e.getMessage());
            }
        } else {
            block();
        }
    }
}