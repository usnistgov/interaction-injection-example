package gov.nist.hla.ii;

import gov.nist.hla.ii.exception.PropertyNotAssigned;
import gov.nist.hla.ii.exception.PropertyNotFound;
import gov.nist.hla.ii.exception.RTIAmbassadorException;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleInject implements Runnable {

	private static final Logger log = LogManager.getLogger(ExampleInject.class);

	InjectionFederate federate;
	InterObjInjection inj;

	Set<InterObjDef> intObjs = new HashSet<InterObjDef>();

	public void init(String configPath) {
		try {
			// We instantiate the injection federate, configure it and
			// initialize it in that order.
			federate = new InjectionFederate();
			federate.loadConfiguration(configPath);
			inj = new InterObjInjectionImpl(federate);
			federate.setInterObjectInjection(inj);

			// We must set an object to receive interactions and object sent by
			// other federates.
			ExampleReception recp = new ExampleReception();
			federate.setInterObjectReception(recp);
			federate.init();

			// We define all interactions and objects we intend to inject. These
			// must match what is in the FOM file.
			// Names must be formatted correctly.
			String interactionName = federate.formatInteractionName("Int1");
			InterObjDef interactionDef = new InterObjDef(interactionName,
					new HashMap<String, String>(), InterObjDef.TYPE.INTERACTION);
			intObjs.add(interactionDef);
			log.debug("staged interaction=" + interactionDef);

			// Names must be formatted correctly.
			String className = federate.formatObjectName("Obj1");
			Map<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("Obj1Attr1", "XXX");
			InterObjDef objectDef = new InterObjDef(className,
					attrMap, InterObjDef.TYPE.OBJECT);
			intObjs.add(objectDef);
			log.debug("staged object=" + objectDef);

			// The injector must be in its own thread.
			Thread th = new Thread(federate);
			th.start();
		} catch (RTIAmbassadorException | ParserConfigurationException
				| IOException | PropertyNotFound | PropertyNotAssigned e) {
			log.error(e);
		}
	}

	public void run() {

		double timeStep = 0D;
		log.info("timeStep=" + timeStep);
		while (federate.getState() != InjectionFederate.State.TERMINATING) {
			// In this example we run through our collection of federates.
			// Here, we are staging each one to be sent by the injector in the
			// current time step.
			for (InterObjDef def : intObjs) {
				inj.addInterObj(def);
			}

			// We tell the injector the staging in done and to advance its
			// time step
			timeStep = federate.startLogicalTime();
			while (federate.getAdvancing().get()) {
				try {
					log.trace("sleeping==>");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error(e);
				}
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
			app.init(args[0]);
			app.run();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
