package gov.nist.hla.ii;

import hla.rti.FederateNotExecutionMember;
import hla.rti.NameNotFound;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIinternalError;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleInject extends InterObjInjectionImpl {

	private static final Logger log = LogManager.getLogger(ExampleInject.class);

	InjectionFederate federate;

	public ExampleInject(InjectionFederate federate) {
		super();
		this.federate = federate;
	}
	
	@Override
	public Queue<InterObjDef> getPreSynchInteractions() {
		String interactionName = federate.formatInteractionName("ExperimentInit");
		Map<String, String> parameters = new HashMap<String, String>();
		// parameters.put("name", "value");
		addInteraction(interactionName, parameters);
	}

	@Override
	public Queue<InterObjDef> getPublications(Double logicalTime) {
		log.trace("getInteractions==>");

		try {
			// We define all interactions and objects we intend to inject. These
			// must match what is in the FOM file.
			// Names must be formatted correctly.
			String interactionName = federate.formatInteractionName("Int1");
			addInteraction(interactionName, new HashMap<String, String>());

			// Names must be formatted correctly.
			String className = federate.formatObjectName("Obj1");
			Map<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("Obj1Attr1", "XXX");
			addObject(className, attrMap);

		} catch (NameNotFound | FederateNotExecutionMember | RTIinternalError | ObjectNotKnown e) {
			log.error(e);
		}
		return publications;
	}
}
