package practica.behaviours;

import java.io.IOException;
import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import practica.model.NewsItem;

public class FetchNewsBehaviour extends TickerBehaviour {
    private int contadorNoticias = 1;
    
    public FetchNewsBehaviour(Agent a, long period) {
    	super(a, period);
    }
    
    protected void onTick() {
    	
    	NewsItem noticiaPrueba = crearNoticiaPrueba();
    	ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    	msg.addReceiver(new AID("coordinador-agent", AID.ISLOCALNAME));
    	
    	try {
    		msg.setContentObject(noticiaPrueba);
    		myAgent.send(msg);
    		System.out.println("[ACQUISITION] Noticia enviada al coordinador: " + noticiaPrueba.getTitle());
    	}
    	catch(IOException e) {
    		System.err.println("[ACQUISITION] Error al serializar la noticia: " + e.getMessage());
    	}
    	
    }
    
    // Metodo auxiliar para generar noticias de prueba distintas cada vez
    private NewsItem crearNoticiaPrueba() {
    	String id = UUID.randomUUID().toString();
    	String title = "Noticia de prueba #" + contadorNoticias;
    	String content = "El alcalde ha declarado que los patos dominarán el mundo. Esto es un caso de prueba";
    	String source = (contadorNoticias % 2 == 0) ? "El Mundo Today" : "BBC News";
    	
    	contadorNoticias++;
    	
    	return new NewsItem(id, title, content, source, "Autor Anonimo", "https://falsaurl.com/" + id, "2023-10-25");
    }
}
