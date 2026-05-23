package practica.agentes;

import jade.core.Agent;
import practica.behaviour.DetectarSesgoBehaviour;
import practica.utils.UtilsDF;

public class AgenteDetectorSesgo extends Agent {

    @Override
    protected void setup() {
        System.out.println("[DETECTAR SESGO] Agente arrancado: " + getLocalName());
        UtilsDF.registrarServicio(this, "deteccion-sesgo", getLocalName());
        addBehaviour(new DetectarSesgoBehaviour(this));
    }

    @Override
    protected void takeDown() {
        UtilsDF.darDeBaja(this);
        System.out.println("[DETECTAR SESGO] Agente apagado: " + getLocalName());
    }
}