package practica.agentes;

import jade.core.Agent;
import practica.comportamiento.ComportamientoObtenerNoticiasRSS; 

public class AgenteAdquisicionRSS extends Agent { 

    @Override
    protected void setup() {
        System.out.println("[ADQUISICION-RSS] ¡Hola! El agente " + getLocalName() + " ha arrancado."); 
        addBehaviour(new ComportamientoObtenerNoticiasRSS(this, 20000)); 
    }

    @Override
    protected void takeDown() {
        System.out.println("[ADQUISICION-RSS] El agente " + getLocalName() + " se está apagando.");
    }
}