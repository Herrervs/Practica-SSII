package practica.agentes;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import practica.behaviour.ObtenerNoticiasBehaviour;

public class AgenteAdquisicion extends Agent {

    @Override
    protected void setup() {
    	System.out.println("[ADQUISICION] ¡Hola! El agente " + getLocalName() + " ha arrancado.");
    	
    	// Retraso inicial de 0 segundos, periodo de 10 segundos
    	addBehaviour(new WakerBehaviour(this, 0) {
            @Override
            protected void onWake() {
                myAgent.addBehaviour(new ObtenerNoticiasBehaviour(myAgent, 10000));
            }
        });
    }
    
    @Override
    protected void takeDown() {
    	System.out.println("[ADQUISICION] El agente " + getLocalName() + " se está apagando.");
    }
}
