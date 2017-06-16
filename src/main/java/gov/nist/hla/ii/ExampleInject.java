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
	InterObjInjection intObjs = new InterObjInjectionImpl();

	public ExampleInject(InjectionFederate federate) {
		super();
		this.federate = federate;
	}

	// public void init() {
	// try {
	// // We define all interactions and objects we intend to inject. These
	// // must match what is in the FOM file.
	// // Names must be formatted correctly.
	// String interactionName = federate.formatInteractionName("Int1");
	// intObjs.addInteraction(interactionName, new HashMap<String, String>());
	//
	// // Names must be formatted correctly.
	// String className = federate.formatObjectName("Obj1");
	// Map<String, String> attrMap = new HashMap<String, String>();
	// attrMap.put("Obj1Attr1", "XXX");
	// intObjs.addObject(className, attrMap);
	//
	// } catch (NameNotFound | FederateNotExecutionMember | RTIinternalError |
	// ObjectNotKnown e) {
	// log.error(e);
	// }
	// }

	// public InterObjInjection getIntObjs() {
	// return intObjs;
	// }

	@Override
	public Queue<InterObjDef> getInteractions() {
		log.trace("getInteractions==>");
		try {
			// We define all interactions and objects we intend to inject. These
			// must match what is in the FOM file.
			// Names must be formatted correctly.
			String interactionName = federate.formatInteractionName("Int1");
			intObjs.addInteraction(interactionName,
					new HashMap<String, String>());

			// Names must be formatted correctly.
			String className = federate.formatObjectName("Obj1");
			Map<String, String> attrMap = new HashMap<String, String>();
			attrMap.put("Obj1Attr1", "XXX");
			intObjs.addObject(className, attrMap);

		} catch (NameNotFound | FederateNotExecutionMember | RTIinternalError
				| ObjectNotKnown e) {
			log.error(e);
		}
		return intObjs.getInteractions();
	}
}
