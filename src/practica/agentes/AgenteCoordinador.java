package practica.agentes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jade.core.Agent;
import practica.behaviour.AgregarResultadosBehaviour;
import practica.behaviour.DelegarAnalisisBehaviour;
import practica.modelo.Noticia;

public class AgenteCoordinador extends Agent {
    private final Map<String, Noticia> noticiasPendientes = new ConcurrentHashMap<>();

    protected void setup() {
        System.out.println("[COORDINADOR] ¡Hola! El agente" + getLocalName() + " ha arrancado.");

        // Escucha noticias nuevas y las reparte a los 3 expertos
        addBehaviour(new DelegarAnalisisBehaviour(this, noticiasPendientes));

        // Escucha las notas de los expertos, las suma y crea el informe final
        addBehaviour(new AgregarResultadosBehaviour(this, noticiasPendientes));
    }

    protected void takeDown() {
        System.out.println("[COORDINADOR] El agente " + getLocalName() + " se está apagando.");
    }

}
