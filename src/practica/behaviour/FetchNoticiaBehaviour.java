package practica.behaviour;

import java.io.IOException;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.modelo.Noticia;

public class FetchNoticiaBehaviour extends TickerBehaviour {
	private int contadorNoticias = 0;

	// Nuestro banco de pruebas con diferentes niveles de sesgo, sentimiento y
	// fuentes
	private final List<Noticia> bancoDeNoticias = Arrays.asList(
			new Noticia("", "El alcalde adopta un pato",
					"Una noticia normal y sin sesgo. El alcalde ha decidido adoptar un pato como mascota del ayuntamiento.",
					"bbc.com", "Autor Anonimo", "https://url1.com", "2023-10-25"),

			new Noticia("", "¡ESCÁNDALO INDIGNANTE!",
					"Es indignante y vergonzoso lo que está pasando. Obviamente, el gobierno oculta la cruda realidad. Esto es un escándalo sin precedentes.",
					"okdiario.com", "Autor Anonimo", "https://url2.com", "2023-10-25"),

			new Noticia("", "No te lo creerás: el fraude que nos ocultan",
					"Peligro inminente. Todo el mundo habla de esta catástrofe y fraude terrible. Los expertos dicen que es el fin.",
					"elplural.com", "Autor Anonimo", "https://url3.com", "2023-10-26"),

			new Noticia("", "La economía muestra signos de recuperación",
					"Los indicadores muestran una leve mejoría en el sector servicios durante el último trimestre.",
					"reuters.com", "Autor Anonimo", "https://url4.com", "2023-10-26"));

	public FetchNoticiaBehaviour(Agent a, long period) {
		super(a, period);
	}

	protected void onTick() {

		Noticia noticiaPrueba = crearNoticiaPrueba();
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));

		try {
			msg.setContentObject(noticiaPrueba);
			myAgent.send(msg);
			System.out.println("[ACQUISITION] Noticia enviada al coordinador: " + noticiaPrueba.getTitulo());
		} catch (IOException e) {
			System.err.println("[ACQUISITION] Error al serializar la noticia: " + e.getMessage());
		}

	}

	// Metodo auxiliar para generar noticias de prueba distintas cada vez
	private Noticia crearNoticiaPrueba() {
		Noticia base = bancoDeNoticias.get(contadorNoticias % bancoDeNoticias.size());
		contadorNoticias++;

		// Devolvemos una copia de la noticia pero inyectándole un ID único (UUID) nuevo
		return new Noticia(
				UUID.randomUUID().toString(),
				base.getTitulo(),
				base.getContenido(),
				base.getFuente(),
				base.getAutor(),
				base.getUrl(),
				base.getFechaPublicacion());
	}
}
