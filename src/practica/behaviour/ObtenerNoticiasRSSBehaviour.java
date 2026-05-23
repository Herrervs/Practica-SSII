package practica.behaviour;

import java.io.IOException;
import java.util.UUID;
import java.net.URI;
import javax.xml.parsers.DocumentBuilder;             
import javax.xml.parsers.DocumentBuilderFactory;       
import org.w3c.dom.Document;                           
import org.w3c.dom.NodeList; 

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.modelo.Noticia;

public class ObtenerNoticiasRSSBehaviour extends TickerBehaviour {
	private int contadorNoticias = 1;

    private static final String URL_FEED = "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada";

	public ObtenerNoticiasRSSBehaviour(Agent agente, long periodo) {
		super(agente, periodo);
	}

	@Override
	protected void onTick() {
		Noticia noticia = leerNoticiaDeRSS();

         if (noticia == null) return;  

		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		mensaje.addReceiver(new AID("agente-coordinador", AID.ISLOCALNAME));

		try {
			mensaje.setContentObject(noticia);
			myAgent.send(mensaje);
			System.out.println("[ADQUISICION-RSS] Noticia enviada al coordinador: " + noticia.getTitulo());
		} catch (IOException e) {
			System.err.println("[ADQUISICION-RSS] Error al serializar la noticia: " + e.getMessage());
		}
    }

    private Noticia leerNoticiaDeRSS() {
        try {
            DocumentBuilder constructor = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document documento = constructor.parse(URI.create(URL_FEED).toURL().openStream());

            // Los feeds RSS tienen los artículos en etiquetas <item>
            NodeList noticias = documento.getElementsByTagName("item");

            if (contadorNoticias >= noticias.getLength()) contadorNoticias = 0;

            org.w3c.dom.Node noticiaActual = noticias.item(contadorNoticias);
            contadorNoticias++;

            // Dentro de cada <item> hay <title>, <description> y <link>
            String titulo    = noticiaActual.getChildNodes().item(0).getTextContent();
            String contenido = noticiaActual.getChildNodes().item(1).getTextContent();
            String url       = noticiaActual.getChildNodes().item(2).getTextContent();

            return new Noticia(
                UUID.randomUUID().toString(), 
                titulo,
                contenido,
                "elpais.com",                 
                "Desconocido",                 
                url,                           
                "2025-01-01"                  
            );

        } catch (Exception e) {
            System.err.println("[ADQUISICION-RSS] Error leyendo RSS: " + e.getMessage()); 
            return null;                      
        }
    }



    
}