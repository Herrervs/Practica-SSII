package practica.agentes;

import jade.core.Agent;
import practica.behaviour.AgregarResultadosBehaviour;
import practica.behaviour.DelegarAnalisisBehaviour;

public class AgenteCoordinador extends Agent {

    protected void setup() {
        System.out.println("[COORDINADOR] ¡Hola! El agente" + getLocalName() + " ha arrancado.");

        // Escucha noticias nuevas y las reparte a los 3 expertos
        addBehaviour(new DelegarAnalisisBehaviour(this));

        // Escucha las notas de los expertos, las suma y crea el informe final
        addBehaviour(new AgregarResultadosBehaviour(this));
    }

    protected void takeDown() {
        System.out.println("[COORDINADOR] El agente " + getLocalName() + " se está apagando.");
    }

}
