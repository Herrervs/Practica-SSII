package practica.utils;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class UtilsDF {
    private UtilsDF() {
    }

    public static void registrarServicio(Agent agente, String tipoServicio, String nombreServicio) {
        DFAgentDescription descripcion = new DFAgentDescription();
        descripcion.setName(agente.getAID());

        ServiceDescription servicio = new ServiceDescription();
        servicio.setType(tipoServicio);
        servicio.setName(nombreServicio);
        descripcion.addServices(servicio);

        try {
            DFService.register(agente, descripcion);
            System.out.println(agente.getLocalName() + " registró el servicio: " + tipoServicio);
        } catch (FIPAException e) {
            System.err.println(agente.getLocalName() + " no pudo registrar el servicio " + tipoServicio + ": "
                    + e.getMessage());
        }
    }

    public static List<AID> buscarAgentesPorServicio(Agent agente, String tipoServicio) {
        List<AID> agentes = new ArrayList<>();

        DFAgentDescription plantilla = new DFAgentDescription();
        ServiceDescription servicio = new ServiceDescription();
        servicio.setType(tipoServicio);
        plantilla.addServices(servicio);

        try {
            DFAgentDescription[] resultados = DFService.search(agente, plantilla);
            for (DFAgentDescription resultado : resultados) {
                agentes.add(resultado.getName());
            }
        } catch (FIPAException e) {
            System.err.println(agente.getLocalName() + " no pudo buscar el servicio " + tipoServicio + ": "
                    + e.getMessage());
        }

        return agentes;
    }

    public static AID buscarPrimerAgentePorServicio(Agent agente, String tipoServicio) {
        List<AID> agentes = buscarAgentesPorServicio(agente, tipoServicio);
        if (agentes.isEmpty()) {
            return null;
        }
        return agentes.get(0);
    }

    public static void darDeBaja(Agent agente) {
        try {
            DFService.deregister(agente);
            System.out.println(agente.getLocalName() + " se dio de baja del DF");
        } catch (FIPAException e) {
            System.err.println(agente.getLocalName() + " no pudo darse de baja del DF: " + e.getMessage());
        }
    }

}
