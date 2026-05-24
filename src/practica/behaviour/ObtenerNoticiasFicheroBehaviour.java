package practica.behaviour;

import java.io.IOException;
import java.util.UUID;
import java.nio.file.Files;             
import java.nio.file.Paths;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.modelo.Noticia;

public class ObtenerNoticiasFicheroBehaviour extends TickerBehaviour {
	private int contadorNoticias = 0;

	public ObtenerNoticiasFicheroBehaviour(Agent agente, long periodo) {
		super(agente, periodo);
	}

	@Override
	protected void onTick() {
		Noticia noticia = leerNoticiaDeFichero();

         if (noticia == null) return;  

		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		mensaje.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));
		mensaje.setConversationId(noticia.getId());
		mensaje.setOntology(DelegarAnalisisBehaviour.ONTOLOGIA_NOTICIA_NUEVA);

		try {
			mensaje.setContentObject(noticia);
			myAgent.send(mensaje);
			System.out.println("[ADQUISICION-FICHERO] Noticia enviada al coordinador: " + noticia.getTitulo());
		} catch (IOException e) {
			System.err.println("[ADQUISICION-FICHERO] Error al serializar la noticia: " + e.getMessage());
		}
    }
    private Noticia leerNoticiaDeFichero() {
        try {
            List<String> lineas = Files.readAllLines(Paths.get("resources/noticias.txt"));
            lineas.removeIf(linea -> linea.trim().isEmpty() || linea.trim().startsWith("#"));

            if (lineas.isEmpty()) {
                System.err.println("[ADQUISICION-FICHERO] El fichero resources/noticias.txt no contiene noticias");
                return null;
            }

            if (contadorNoticias >= lineas.size()) contadorNoticias = 0;

            String linea = lineas.get(contadorNoticias);
            contadorNoticias++;

            String[] partes = linea.split("\\|", -1);
            if (partes.length < 3) {
                System.err.println("[ADQUISICION-FICHERO] Linea invalida. Formato esperado: titulo|contenido|fuente");
                return null;
            }

            return new Noticia(
                UUID.randomUUID().toString(),  
                partes[0].trim(),
                partes[1].trim(),
                partes[2].trim(),
                "Desconocido",
                "",
                "2025-01-01"
            );

        } catch (Exception e) {
            System.err.println("[ADQUISICION-FICHERO] Error leyendo fichero: " + e.getMessage());
            return null;
        }
    }

}
