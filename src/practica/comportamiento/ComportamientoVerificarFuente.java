package practica.comportamiento;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import practica.modelo.Noticia;
import practica.pln.BDReputacionFuente;

public class ComportamientoVerificarFuente extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    private final BDReputacionFuente validadorFuentes;

    public ComportamientoVerificarFuente(Agent agente) {
        super(agente);
        this.validadorFuentes = new BDReputacionFuente();
    }

    @Override
    public void action() {
        MessageTemplate plantilla = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage peticion = myAgent.receive(plantilla);

        if (peticion != null) {
            try {
                Noticia noticia = (Noticia) peticion.getContentObject();

                double scoreReputacion = validadorFuentes.getPuntuacion(noticia.getFuente());

                ACLMessage respuesta = peticion.createReply();
                respuesta.setPerformative(ACLMessage.INFORM);
                respuesta.setContent(String.valueOf(scoreReputacion));
                
                myAgent.send(respuesta);
                System.out.println("[" + myAgent.getLocalName() + "] -> Reputación de fuente verificada para '" 
                        + noticia.getFuente() + "' (Score: " + scoreReputacion + ")");

            } catch (Exception e) {
                System.err.println("[" + myAgent.getLocalName() + "] Fallo en comportamiento verificador: " 
                        + e.getMessage());
            }
        } else {
            block();
        }
    }
}