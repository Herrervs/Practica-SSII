package practica.agentes;

import jade.core.Agent;
import practica.behaviour.ObtenerNoticiasAPIBehaviour;

public class AgenteAdquisicionAPI extends Agent { 

    @Override
    protected void setup() {
        System.out.println("[ADQUISICION-API] ¡Hola! El agente " + getLocalName() + " ha arrancado."); 
        addBehaviour(new ObtenerNoticiasAPIBehaviour(this, 25000)); 
    }

    @Override
    protected void takeDown() {
        System.out.println("[ADQUISICION-API] El agente " + getLocalName() + " se está apagando."); 
    }
}