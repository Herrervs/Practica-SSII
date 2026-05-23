package practica.agentes;

import jade.core.Agent;
import practica.behaviour.ActualizarUIBehaviour;

public class AgenteVisualizacion extends Agent {

    protected void setup() {
        System.out.println("[VISUALIZACION] ¡Hola! El agente" + getLocalName() + " ha arrancado.");

        // Lanzamos la ventana grafica y nos quedamos escuchando informes
        addBehaviour(new ActualizarUIBehaviour(this));
    }

    protected void takeDown() {
        System.out.println("[VISUALIZACION] El agente " + getLocalName() + " se está apagando.");
    }

}
