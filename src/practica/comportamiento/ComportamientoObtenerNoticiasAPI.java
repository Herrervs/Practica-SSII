package practica.comportamiento;

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

public class ComportamientoObtenerNoticiasAPI extends TickerBehaviour {
	private int contadorNoticias = 1;
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=es&apiKey=VUESTRA_API_KEY";

	public ComportamientoObtenerNoticiasAPI(Agent agente, long periodo) {
		super(agente, periodo);
	}

	@Override
	protected void onTick() {
		Noticia noticia = leerNoticiaDeAPI();

         if (noticia == null) return;  

		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		mensaje.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));

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
            // Hacer la petición HTTP GET
            HttpURLConnection conexion = (HttpURLConnection) new URI(API_URL).toURL().openConnection();
            conexion.setRequestMethod("GET");

            // Leer la respuesta completa
            Scanner scanner = new Scanner(conexion.getInputStream());
            StringBuilder respuesta = new StringBuilder();
            while (scanner.hasNext()) respuesta.append(scanner.nextLine());
            scanner.close();

            String json = respuesta.toString();

            // El JSON de NewsAPI tiene: "articles":[{"title":"...","description":"...","url":"...","source":{"name":"..."}}]
            String[] articulos = json.split("\"title\":");

            if (contadorNoticias >= articulos.length) contadorNoticias = 1;

            String bloque = articulos[contadorNoticias];
            contadorNoticias++;

            // Extraer cada campo entre comillas
            String titulo    = bloque.split("\"")[1];
            String contenido = bloque.split("\"description\":\"")[1].split("\"")[0];
            String url       = bloque.split("\"url\":\"")[1].split("\"")[0];
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