package practica.agentes;

import jade.core.Agent;
import practica.comportamiento.ComportamientoAnalizarSentimiento;
import practica.utils.UtilsDF;

public class AgenteSentimiento extends Agent {

    @Override
    protected void setup() {
        System.out.println("[ANALIZAR SENTIMIENTO] Agente arrancado: " + getLocalName());
        UtilsDF.registrarServicio(this, "analisis-sentimiento", getLocalName());
        addBehaviour(new ComportamientoAnalizarSentimiento(this));
    }

    @Override
    protected void takeDown() {
        UtilsDF.darDeBaja(this);
        System.out.println("[ANALIZAR SENTIMIENTO] Agente apagado: " + getLocalName());
    }
}