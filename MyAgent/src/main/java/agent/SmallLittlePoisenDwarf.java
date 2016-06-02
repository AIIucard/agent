package main.java.agent;

import de.aim.antworld.agent.AntWorldConsts;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import main.java.gui.DwarfVisualCenter;
import main.java.utils.DwarfUtils;

public class SmallLittlePoisenDwarf extends Agent implements InterfaceAgent {
	private static final long serialVersionUID = 1L;

	private String name;
	private DwarfVisualCenter dwarfVisualCenter;
	// "%-5p [%-20C%d{dd MMM yyyy HH:mm:ss,SSS}]: %m%n"
	private TopicManagementHelper hlp;
	private AID antWorldGameLeaderAID;

	@Override
	protected void setup() {
		name = getLocalName();
		dwarfVisualCenter = DwarfVisualCenter.getInstance();

		// Location loc = here();
		//
		// // some info about me
		// System.out.println(name + ": my local name is " + name);
		// System.out.println(name + ": my global name is " + getName());
		// System.out.println(name + ": my state is " + getAgentState());
		// // some info about my location
		// System.out.println(name + ": my location is " + loc.getName());
		// System.out.println(name + ": the id of my location is " +
		// loc.getID());
		// System.out.println(name + ": the address of my location is "
		// + loc.getAddress());
		// System.out.println(name + ": the protocol of the location is "
		// + loc.getProtocol());

		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {

				try {
					System.out.println("\n------------------------------");
					System.out.println("Searching for agents...");

					System.out.println("---Set filter: " + AntWorldConsts.SEVICE_NAME + " for search.");
					ServiceDescription filter = new ServiceDescription();
					filter.setType(AntWorldConsts.SEVICE_NAME);
					DFAgentDescription dfd = new DFAgentDescription();
					dfd.addServices(filter);

					System.out.println("---Search started...");
					DFAgentDescription[] results = DFService.search(myAgent, dfd);

					for (int i = 0; i < results.length; ++i) {
						System.out.println("---" + results[i].getName().getLocalName() + ":");
						if (DwarfUtils.containsString(results[i].getName().getLocalName(), "antWorld")) {
							antWorldGameLeaderAID = new AID(results[i].getName().getLocalName(), AID.ISLOCALNAME);
							System.out.println("---GameLeaderAgent found: " + antWorldGameLeaderAID.getLocalName());
							break;
						}
					}
					if (antWorldGameLeaderAID == null) {
						System.out.println("---No GameLeaderAgent found!");
					}
				} catch (Exception e) {
					System.out.println("GameLeaderAgent not found! " + e.getStackTrace().toString());
				}
				System.out.println("Searching for agents finished!");
				System.out.println("------------------------------\n");

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
					System.out.println("Receive message:\n" + msg + "\n");
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
		dwarfVisualCenter.getAgents().remove(name);
	};
}
