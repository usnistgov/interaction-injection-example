package gov.nist.hla.ii;

import gov.nist.hla.ii.exception.PropertyNotAssigned;
import gov.nist.hla.ii.exception.PropertyNotFound;
import gov.nist.hla.ii.exception.RTIAmbassadorException;
import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIinternalError;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleInject implements Runnable {

	private static final Logger log = LogManager.getLogger(ExampleInject.class);

	InjectionFederate federate;
	InterObjInjection inj;

	Set<InterObjDef> intObjs = new HashSet<InterObjDef>();

	public void init(String[] args) {
		try {
			// We instantiate the injection federate and configure it and
			// initialize it in that order.
			federate = new InjectionFederate();
			federate.loadConfiguration(args[0]);
			inj = new InterObjInjectionImpl(federate);
			federate.setInterObjectInjection(inj);
			federate.init();

			// We define all interactions and objects we intend to inject. These
			// must match what is in the FOM file.
			// Names must be formatted correctly.
			String interactionName = inj.formatInteractionName("Int1");
			InterObjDef interactionDef = new InterObjDef(interactionName,
					new HashMap<String, String>(), InterObjDef.TYPE.INTERACTION);
			intObjs.add(interactionDef);
			federate.publishInteraction(interactionName);

			// Names must be formatted correctly.
			String className = inj.formatObjectName("Obj1");
			Map<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("Obj1Attr1", "XXX");
			InterObjDef objectDef = new InterObjDef(interactionName,
					new HashMap<String, String>(), InterObjDef.TYPE.OBJECT);
			intObjs.add(objectDef);

			// We must set an object to receive interactions and object sent by
			// other federates.
			ExampleReception recp = new ExampleReception();
			federate.setInterObjectReception(recp);

			// The injector must be in its own thread.
			Thread th = new Thread(federate);
			th.start();
		} catch (RTIAmbassadorException | ParserConfigurationException
				| IOException | PropertyNotFound | PropertyNotAssigned e) {
			log.error(e);
		}

	}

	public void run() {

		double timeStep = 1D;
		while (true) {
			// In this example we run through our collection of federates.
			// Here, we are staging each one to be sent byt the injector in the current time step.
			for (InterObjDef def : intObjs) {
				inj.addInterObj(def);
			}

			try {
				// We tell the injector the staging in done and to advance its time step
				timeStep = federate.advanceLogicalTime();
			} catch (RTIAmbassadorException e) {
				log.error(e);
			}
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			log.error("command line argument for properties file not specified");
			args = new String[1];
			args[0] = "config.properties";
		}

		try {
			ExampleInject app = new ExampleInject();
			app.init(args);
			app.run();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
