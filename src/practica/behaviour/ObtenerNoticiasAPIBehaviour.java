package practica.behaviour;

import java.io.IOException;
import java.util.UUID;
import java.net.HttpURLConnection;             
import java.net.URI; 
import java.util.Scanner; 

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.modelo.Noticia;

public class ObtenerNoticiasAPIBehaviour extends TickerBehaviour {
	private int contadorNoticias = 1;
    private String jsonGuardado = null; 
    private static final String API_URL = "https://newsapi.org/v2/everything?q=spain&language=es&pageSize=20&apiKey=06c8e1c18cfa436bb1f4081508a8f9a2";

	public ObtenerNoticiasAPIBehaviour(Agent agente, long periodo) {
		super(agente, periodo);
	}

	@Override
	protected void onTick() {
		Noticia noticia = leerNoticiaDeAPI();

         if (noticia == null) return;  

		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		mensaje.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));
		mensaje.setConversationId(noticia.getId());
		mensaje.setOntology(DelegarAnalisisBehaviour.ONTOLOGIA_NOTICIA_NUEVA);

		try {
			mensaje.setContentObject(noticia);
			myAgent.send(mensaje);
			System.out.println("[ADQUISICION-API] Noticia enviada al coordinador: " + noticia.getTitulo());
		} catch (IOException e) {
			System.err.println("[ADQUISICION-API] Error al serializar la noticia: " + e.getMessage());
		}
    }

    private Noticia leerNoticiaDeAPI() {
        try {
            if (jsonGuardado == null) {
                // Hacer la petición HTTP GET
                HttpURLConnection conexion = (HttpURLConnection) new URI(API_URL).toURL().openConnection();
                conexion.setRequestMethod("GET");

                // Leer la respuesta completa
                Scanner scanner = new Scanner(conexion.getInputStream());
                StringBuilder respuesta = new StringBuilder();
                while (scanner.hasNext()) respuesta.append(scanner.nextLine());
                scanner.close();
                jsonGuardado = respuesta.toString(); 
            }
            // El JSON de NewsAPI tiene: "articles":[{"title":"...","description":"...","url":"...","source":{"name":"..."}}]
            String[] articulos = jsonGuardado.split("\\{\"source\"");
            if (contadorNoticias >= articulos.length) contadorNoticias = 1;
            String bloque = articulos[contadorNoticias];
            contadorNoticias++;
            String sinSource = bloque.split("}")[1];


            // Extraer cada campo entre comillas
            String titulo = sinSource.split("\"title\":\"")[1].split("\"")[0];
            String contenido = sinSource.split("\"description\":\"")[1].split("\"")[0];
            String url       = sinSource.split("\"url\":\"")[1].split("\"")[0];
            String fuente    = bloque.split("\"name\":\"")[1].split("\"")[0];
            return new Noticia(
                UUID.randomUUID().toString(),  
                titulo,
                contenido,
                fuente,                        
                "Desconocido",               
                url,                           
                "2025-01-01"                  
            );

        } catch (Exception e) {
            System.err.println("[ADQUISICION-API] Error llamando a la API: " + e.getMessage()); 
            return null;                       
        }
    }


}
