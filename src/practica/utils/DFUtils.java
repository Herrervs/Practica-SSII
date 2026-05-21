package practica.utils;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFUtils {
    private DFUtils() {
    }

    public static void registerService(Agent agent, String serviceType, String serviceName) {
        DFAgentDescription description = new DFAgentDescription();
        description.setName(agent.getAID());

        ServiceDescription service = new ServiceDescription();
        service.setType(serviceType);
        service.setName(serviceName);
        description.addServices(service);

        try {
            DFService.register(agent, description);
            System.out.println(agent.getLocalName() + " registered service: " + serviceType);
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + " could not register service " + serviceType + ": "
                    + e.getMessage());
        }
    }

    public static List<AID> searchAgentsByService(Agent agent, String serviceType) {
        List<AID> agents = new ArrayList<>();

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription service = new ServiceDescription();
        service.setType(serviceType);
        template.addServices(service);

        try {
            DFAgentDescription[] results = DFService.search(agent, template);
            for (DFAgentDescription result : results) {
                agents.add(result.getName());
            }
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + " could not search service " + serviceType + ": "
                    + e.getMessage());
        }

        return agents;
    }

    public static AID findFirstAgentByService(Agent agent, String serviceType) {
        List<AID> agents = searchAgentsByService(agent, serviceType);
        if (agents.isEmpty()) {
            return null;
        }
        return agents.get(0);
    }

    public static void deregister(Agent agent) {
        try {
            DFService.deregister(agent);
            System.out.println(agent.getLocalName() + " deregistered from DF");
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + " could not deregister from DF: " + e.getMessage());
        }
    }

}
