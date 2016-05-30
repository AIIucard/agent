package main.java.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;

import main.java.gui.DwarfVisualCenter;

import org.json.JSONException;
import org.json.JSONObject;

public class SmallLittlePoisenDwarf extends Agent implements InterfaceAgent {
	private static final long serialVersionUID = 1L;

	private String name;
	private DwarfVisualCenter dwarfVisualCenter;
	private JSONObject obj;
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
				// create and config message
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setSender(getAID());

				// try {
				// hlp = (TopicManagementHelper)
				// getHelper(TopicManagementHelper.SERVICE_NAME);
				// antServiceTopic = hlp.createTopic("antWorld2016");
				// } catch (ServiceException e1) {
				// System.out.println("Topic not found! " +
				// e1.getStackTrace().toString());
				// }

				try {
					System.out.println("searching for agents...");
					// create filter for service1
					ServiceDescription filter = new ServiceDescription();
					filter.setType("antWorld2016");
					DFAgentDescription dfd = new DFAgentDescription();
					dfd.addServices(filter);
					// search for agents and services
					DFAgentDescription[] results = DFService.search(myAgent, dfd);
					for (int i = 0; i < results.length; ++i) {
						System.out.println(results[i].getName().getLocalName() + ":");
						Iterator it = results[i].getAllServices();
						while (it.hasNext()) {
							ServiceDescription sd = (ServiceDescription) it.next();
							System.out.println(" - " + sd.getName());
							antWorldGameLeaderAID = new AID("sd.getOwnership()", AID.ISLOCALNAME);
						}
					}
					System.out.println();
				} catch (Exception e) {
					System.out.println("GameLeaderAgent not found! " + e.getStackTrace().toString());
				}

				msg.addReceiver(antWorldGameLeaderAID);
				msg.setReplyWith("request");

				try {
					obj = new JSONObject();
					obj.put("color", "ANT_COLOR_GREEN");
					obj.put("type", "ANT_ACTION_LOGIN");
				} catch (JSONException e) {
					System.out.println("JSON message can not be generated! " + e.getStackTrace().toString());
				}
				if (obj != null && antWorldGameLeaderAID != null) {
					System.out.println(obj.toString());
					msg.setContent(obj.toString());
					send(msg);
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
