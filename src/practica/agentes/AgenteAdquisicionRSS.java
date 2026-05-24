package practica.agentes;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import practica.behaviour.ObtenerNoticiasRSSBehaviour; 

public class AgenteAdquisicionRSS extends Agent { 

    @Override
    protected void setup() {
        System.out.println("[ADQUISICION-RSS] ¡Hola! El agente " + getLocalName() + " ha arrancado."); 
        
        // Retraso inicial de 5 segundos, periodo de 10 segundos
        addBehaviour(new WakerBehaviour(this, 5000) {
            @Override
            protected void onWake() {
                myAgent.addBehaviour(new ObtenerNoticiasRSSBehaviour(myAgent, 10000));
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("[ADQUISICION-RSS] El agente " + getLocalName() + " se está apagando.");
    }
}