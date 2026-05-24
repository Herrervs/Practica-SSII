package practica.agentes;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import practica.behaviour.ObtenerNoticiasFicheroBehaviour;

public class AgenteAdquisicionFichero extends Agent {

    @Override
    protected void setup() {
    	System.out.println("[ADQUISICION-FICHERO] ¡Hola! El agente " + getLocalName() + " ha arrancado.");
    	
    	// Retraso inicial de 2.5 segundos, periodo de 10 segundos
    	addBehaviour(new WakerBehaviour(this, 2500) {
            @Override
            protected void onWake() {
                myAgent.addBehaviour(new ObtenerNoticiasFicheroBehaviour(myAgent, 10000));
            }
        });
    }
    
    @Override
    protected void takeDown() {
    	System.out.println("[ADQUISICION-FICHERO] El agente " + getLocalName() + " se está apagando.");
    }
}
