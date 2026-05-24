package practica.agentes;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import practica.behaviour.ObtenerNoticiasAPIBehaviour;

public class AgenteAdquisicionAPI extends Agent { 

    @Override
    protected void setup() {
        System.out.println("[ADQUISICION-API] ¡Hola! El agente " + getLocalName() + " ha arrancado."); 
        
        // Retraso inicial de 7.5 segundos, periodo de 10 segundos
        addBehaviour(new WakerBehaviour(this, 7500) {
            @Override
            protected void onWake() {
                myAgent.addBehaviour(new ObtenerNoticiasAPIBehaviour(myAgent, 10000));
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("[ADQUISICION-API] El agente " + getLocalName() + " se está apagando."); 
    }
}