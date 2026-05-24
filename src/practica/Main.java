package practica;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.OutputStream;
import java.io.PrintStream;

public class Main {
    private static final String AGENTE_ADQUISICION = "practica.agentes.AgenteAdquisicion";
    private static final String AGENTE_ADQUISICION_FICHERO = "practica.agentes.AgenteAdquisicionFichero";
    private static final String AGENTE_COORDINADOR = "practica.agentes.AgenteCoordinador";
    private static final String AGENTE_SENTIMIENTO = "practica.agentes.AgenteSentimiento";
    private static final String AGENTE_SESGO = "practica.agentes.AgenteDetectorSesgo";
    private static final String AGENTE_FUENTE = "practica.agentes.AgenteVerificadorFuente";
    private static final String AGENTE_VISUALIZACION = "practica.agentes.AgenteVisualizacion";

    public static void main(String[] args) {
        // Filtro para ocultar el falso error de Base64 de JADE 4.6.0 en Java modernos
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            private StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                if (b == '\n') {
                    String line = buffer.toString();
                    if (!line.contains("Missing support for Base64") &&
                            !line.contains("===== E R R O R !!!") &&
                            !line.contains("Please refer to the documentation") &&
                            !line.contains("=============================================")) {
                        originalErr.println(line);
                    }
                    buffer.setLength(0);
                } else if (b != '\r') {
                    buffer.append((char) b);
                }
            }
        }));

        try {
            AgentContainer contenedor = crearContenedorPrincipal(deberiaMostrarGui(args));

            iniciarAgente(contenedor, "agente-adquisicion", AGENTE_ADQUISICION);
            iniciarAgente(contenedor, "agente-adquisicion-fichero", AGENTE_ADQUISICION_FICHERO);
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
