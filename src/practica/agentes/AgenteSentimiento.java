package practica.agentes;

import jade.core.Agent;
import practica.behaviour.AnalizarSentimientoBehaviour;
import practica.utils.UtilsDF;

public class AgenteSentimiento extends Agent {

    @Override
    protected void setup() {
        System.out.println("[ANALIZAR SENTIMIENTO] Agente arrancado: " + getLocalName());
        UtilsDF.registrarServicio(this, "analisis-sentimiento", getLocalName());
        addBehaviour(new AnalizarSentimientoBehaviour(this));
    }

    @Override
    protected void takeDown() {
        UtilsDF.darDeBaja(this);
        System.out.println("[ANALIZAR SENTIMIENTO] Agente apagado: " + getLocalName());
    }
}