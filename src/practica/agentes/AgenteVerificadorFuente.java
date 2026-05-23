package practica.agentes;

import jade.core.Agent;
import practica.behaviour.VerificarFuenteBehaviour;
import practica.utils.UtilsDF;

public class AgenteVerificadorFuente extends Agent {

    @Override
    protected void setup() {
        System.out.println("[VERIFICAR FUENTE] Agente arrancado: " + getLocalName());
        UtilsDF.registrarServicio(this, "verificacion-fuente", getLocalName());
        addBehaviour(new VerificarFuenteBehaviour(this));
    }

    @Override
    protected void takeDown() {
        UtilsDF.darDeBaja(this);
        System.out.println("[VERIFICAR FUENTE] Agente apagado: " + getLocalName());
    }
}