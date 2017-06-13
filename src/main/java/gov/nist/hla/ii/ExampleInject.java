package gov.nist.hla.ii;

import gov.nist.hla.ii.exception.PropertyNotAssigned;
import gov.nist.hla.ii.exception.PropertyNotFound;
import gov.nist.hla.ii.exception.RTIAmbassadorException;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectClassNotPublished;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIinternalError;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleInject implements Runnable {

	private static final Logger log = LogManager.getLogger(ExampleInject.class);

	InjectionFederate federate;
	InterObjInjection inj;

	public void init(String[] args) {
		try {
			federate = new InjectionFederate();
			federate.loadConfiguration(args[0]);
			inj = new InterObjInjectionImpl(federate);
			federate.setInterObjectInjection(inj);
			federate.init();
			String interactionName = inj.formatInteractionName("Int1");
			federate.publishInteraction(interactionName);
			String className = inj.formatObjectName("Obj1");
			Map<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("Obj1Attr1", "XXX");
			int classHandle = federate.publishObject(className, attrMap);
			log.debug("classHandle=" + classHandle);
			int objectHandle = federate.registerObject(className, classHandle);
			log.debug("objectHandle=" + objectHandle);
			ExampleReception recp = new ExampleReception();
			federate.setInterObjectReception(recp);
			Thread th = new Thread(federate);
			th.start();
		} catch (RTIAmbassadorException | ParserConfigurationException
				| IOException | PropertyNotFound | PropertyNotAssigned e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {

		double timeStep = 1D;
		while (true) {
			String interactionName = inj.formatInteractionName("Int1");
			log.trace("injecting interactionName=" + interactionName);
			Map<String, String> paramMap = new HashMap<String, String>();
			inj.addInteraction(interactionName, paramMap);

			String objectName = inj.formatObjectName("Obj1");
			Map<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("Obj1Attr1", "XXX-" + timeStep);

			try {
				inj.addObject(objectName, attrMap);
			} catch (NameNotFound | FederateNotExecutionMember
					| RTIinternalError | ObjectNotKnown e) {
				log.error(e);
			}
			try {
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
