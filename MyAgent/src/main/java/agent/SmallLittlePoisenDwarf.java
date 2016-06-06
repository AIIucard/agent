package main.java.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import main.java.utils.DwarfUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.aim.antworld.agent.AntWorldConsts;

public class SmallLittlePoisenDwarf extends Agent implements InterfaceAgent {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

	private String name;
	// "%-5p [%-20C%d{dd MMM yyyy HH:mm:ss,SSS}]: %m%n"
	private AID antWorldGameLeaderAID;

	@Override
	protected void setup() {
		// PropertyConfigurator.configure("./src/main/java/cfg/log4j.properties");
		name = getLocalName();

		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {

				try {
					log.info("\n------------------------------");
					log.info("Searching for agents...");

					log.info("---Set filter: " + AntWorldConsts.SEVICE_NAME + " for search.");
					ServiceDescription filter = new ServiceDescription();
					filter.setType(AntWorldConsts.SEVICE_NAME);
					DFAgentDescription dfd = new DFAgentDescription();
					dfd.addServices(filter);

					log.info("---Search started...");
					DFAgentDescription[] results = DFService.search(myAgent, dfd);

					for (int i = 0; i < results.length; ++i) {
						log.info("---" + results[i].getName().getLocalName() + ":");
						if (DwarfUtils.containsString(results[i].getName().getLocalName(), "antWorld")) {
							antWorldGameLeaderAID = new AID(results[i].getName().getLocalName(), AID.ISLOCALNAME);
							log.info("---GameLeaderAgent found: " + antWorldGameLeaderAID.getLocalName());
							break;
						}
					}
					if (antWorldGameLeaderAID == null) {
						log.error("---No GameLeaderAgent found!");
					}
				} catch (Exception e) {
					log.error("GameLeaderAgent not found! " + e.getStackTrace().toString());
				}
				log.info("Searching for agents finished!");
				log.info("------------------------------\n");

				// create and config message
				if (antWorldGameLeaderAID != null) {
					ACLMessage msg = DwarfUtils.createLoginMessage(antWorldGameLeaderAID, getAID());
					if (msg != null) {
						send(msg);
					}
				}
			}
		});
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					doWait(5000);
					log.info("Receive message:\n" + msg + "\n");
					// ACLMessage reply = msg.createReply();
					// reply.setReplyWith("reply of " + msg.getReplyWith());
					// send(reply);
				} else {
					block();
				}
			}
		});
	}

	@Override
	protected void takeDown() {
		// dwarfVisualCenter.getAgents().remove(name);
	};
}
