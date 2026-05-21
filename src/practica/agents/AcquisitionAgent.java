package practica.agents;

import jade.core.Agent;
import practica.behaviours.FetchNewsBehaviour;

public class AcquisitionAgent extends Agent {
    protected void setup() {
    	System.out.println("[ACQUISITION] Hola! El agente " + getLocalName() + " ha arrancado.");
    	
    	// Añadimos el comportamiento para que genere una noticia cada 10 segundos (10000 ms)
    	addBehaviour(new FetchNewsBehaviour(this, 10000));
    }
    
    protected void takeDown() {
    	System.out.println("[ACQUISITION] El agente " + getLocalName() + " se esta apagando.");
    }
}
