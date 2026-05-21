package practica;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainLauncher {
    private static final String ACQUISITION_AGENT = "practica.agents.AcquisitionAgent";
    private static final String COORDINATOR_AGENT = "practica.agents.CoordinatorAgent";
    private static final String SENTIMENT_AGENT = "practica.agents.SentimentAgent";
    private static final String BIAS_AGENT = "practica.agents.BiasDetectorAgent";
    private static final String SOURCE_AGENT = "practica.agents.SourceVerifierAgent";
    private static final String VISUALIZATION_AGENT = "practica.agents.VisualizationAgent";

    public static void main(String[] args) {
        try {
            AgentContainer container = createMainContainer(shouldShowGui(args));

            startAgent(container, "acquisition-agent", ACQUISITION_AGENT);
            startAgent(container, "coordinator-agent", COORDINATOR_AGENT);
            startAgent(container, "sentiment-agent", SENTIMENT_AGENT);
            startAgent(container, "bias-detector-agent", BIAS_AGENT);
            startAgent(container, "source-verifier-agent", SOURCE_AGENT);
            startAgent(container, "visualization-agent", VISUALIZATION_AGENT);
        } catch (StaleProxyException e) {
            System.err.println("Could not start JADE agents: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static AgentContainer createMainContainer(boolean showGui) {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, Boolean.toString(showGui));
        return runtime.createMainContainer(profile);
    }

    private static AgentController startAgent(AgentContainer container, String localName, String className,
            Object... arguments) throws StaleProxyException {
        AgentController controller = container.createNewAgent(localName, className, arguments);
        controller.start();
        System.out.println("Started agent: " + localName + " (" + className + ")");
        return controller;
    }

    private static boolean shouldShowGui(String[] args) {
        if (args == null) {
            return true;
        }
        for (String arg : args) {
            if ("--nogui".equalsIgnoreCase(arg)) {
                return false;
            }
        }
        return true;
    }
}
