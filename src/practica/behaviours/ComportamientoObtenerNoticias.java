package practica.behaviours;

import java.io.IOException;
import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.model.Noticia;

public class ComportamientoObtenerNoticias extends TickerBehaviour {
	private int contadorNoticias = 1;

	public ComportamientoObtenerNoticias(Agent agente, long periodo) {
		super(agente, periodo);
	}

	@Override
	protected void onTick() {
		Noticia noticiaPrueba = crearNoticiaPrueba();
		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		mensaje.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));

		try {
			mensaje.setContentObject(noticiaPrueba);
			myAgent.send(mensaje);
			System.out.println("[ADQUISICION] Noticia enviada al coordinador: " + noticiaPrueba.getTitulo());
		} catch (IOException e) {
			System.err.println("[ADQUISICION] Error al serializar la noticia: " + e.getMessage());
		}
	}

	// Método auxiliar para generar noticias de prueba distintas cada vez
	private Noticia crearNoticiaPrueba() {
		String id = UUID.randomUUID().toString();
		String titulo = "Noticia de prueba #" + contadorNoticias;
		String contenido = "El alcalde ha declarado que los patos dominarán el mundo. Esto es un caso de prueba";
		String fuente = (contadorNoticias % 2 == 0) ? "El Mundo Today" : "BBC News";

		contadorNoticias++;

		return new Noticia(id, titulo, contenido, fuente, "Autor Anonimo", "https://falsaurl.com/" + id, "2023-10-25");
	}
}
