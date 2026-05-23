package practica.agentes;

import jade.core.Agent;
import practica.behaviour.ObtenerNoticiasFicheroBehaviour;

public class AgenteAdquisicionFichero extends Agent {

    @Override
    protected void setup() {
    	System.out.println("[ADQUISICION-FICHERO] ¡Hola! El agente " + getLocalName() + " ha arrancado.");
    	
    	// Añadimos el comportamiento para que genere una noticia cada 10 segundos (10000 ms)
    	addBehaviour(new ObtenerNoticiasFicheroBehaviour(this, 15000));
    }
    
    @Override
    protected void takeDown() {
    	System.out.println("[ADQUISICION-FICHERO] El agente " + getLocalName() + " se está apagando.");
    }
}
