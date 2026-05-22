package practica.comportamiento;

import java.io.IOException;
import java.util.UUID;
import java.nio.file.Files;             
import java.nio.file.Paths;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.modelo.Noticia;

public class ComportamientoObtenerNoticiasFichero extends TickerBehaviour {
	private int contadorNoticias = 1;

	public ComportamientoObtenerNoticiasFichero(Agent agente, long periodo) {
		super(agente, periodo);
	}

	@Override
	protected void onTick() {
		Noticia noticia = leerNoticiaDeFichero();

         if (noticia == null) return;  

		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		mensaje.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));

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
            String contenidoFichero = new String(Files.readAllBytes(Paths.get("resources/noticias.txt")));
            String[] lineas = contenidoFichero.split("\n");

            if (contadorNoticias >= lineas.length) contadorNoticias = 0;

            String linea = lineas[contadorNoticias];
            contadorNoticias++;

            String[] partes = linea.split("\\|");
            return new Noticia(
                UUID.randomUUID().toString(),  
                partes[0],
                partes[1],
                partes[2],
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