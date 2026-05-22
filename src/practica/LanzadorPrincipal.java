package practica;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class LanzadorPrincipal {
    private static final String AGENTE_ADQUISICION = "practica.agents.AgenteAdquisicion";
    private static final String AGENTE_COORDINADOR = "practica.agents.AgenteCoordinador";
    private static final String AGENTE_SENTIMIENTO = "practica.agents.AgenteSentimiento";
    private static final String AGENTE_SESGO = "practica.agents.AgenteDetectorSesgo";
    private static final String AGENTE_FUENTE = "practica.agents.AgenteVerificadorFuente";
    private static final String AGENTE_VISUALIZACION = "practica.agents.AgenteVisualizacion";

    public static void main(String[] args) {
        try {
            AgentContainer contenedor = crearContenedorPrincipal(deberiaMostrarGui(args));

            iniciarAgente(contenedor, "agente-adquisicion", AGENTE_ADQUISICION);
            iniciarAgente(contenedor, "agente-coordinador", AGENTE_COORDINADOR);
            iniciarAgente(contenedor, "agente-sentimiento", AGENTE_SENTIMIENTO);
            iniciarAgente(contenedor, "agente-detector-sesgo", AGENTE_SESGO);
            iniciarAgente(contenedor, "agente-verificador-fuente", AGENTE_FUENTE);
            iniciarAgente(contenedor, "agente-visualizacion", AGENTE_VISUALIZACION);
        } catch (StaleProxyException e) {
            System.err.println("No se pudieron iniciar los agentes JADE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static AgentContainer crearContenedorPrincipal(boolean mostrarGui) {
        Runtime entorno = Runtime.instance();
        Profile perfil = new ProfileImpl();
        perfil.setParameter(Profile.MAIN_HOST, "localhost");
        perfil.setParameter(Profile.GUI, Boolean.toString(mostrarGui));
        return entorno.createMainContainer(perfil);
    }

    private static AgentController iniciarAgente(AgentContainer contenedor, String nombreLocal, String nombreClase,
            Object... argumentos) throws StaleProxyException {
        AgentController controlador = contenedor.createNewAgent(nombreLocal, nombreClase, argumentos);
        controlador.start();
        System.out.println("Agente iniciado: " + nombreLocal + " (" + nombreClase + ")");
        return controlador;
    }

    private static boolean deberiaMostrarGui(String[] args) {
        if (args == null) {
            return true;
        }
        for (String argumento : args) {
            if ("--nogui".equalsIgnoreCase(argumento)) {
                return false;
            }
        }
        return true;
    }
}
