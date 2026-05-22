package practica.agentes;

import jade.core.Agent;
import practica.comportamiento.ComportamientoObtenerNoticias;

public class AgenteAdquisicion extends Agent {

    @Override
    protected void setup() {
    	System.out.println("[ADQUISICION] ¡Hola! El agente " + getLocalName() + " ha arrancado.");
    	
    	// Añadimos el comportamiento para que genere una noticia cada 10 segundos (10000 ms)
    	addBehaviour(new ComportamientoObtenerNoticias(this, 10000));
    }
    
    @Override
    protected void takeDown() {
    	System.out.println("[ADQUISICION] El agente " + getLocalName() + " se está apagando.");
    }
}
