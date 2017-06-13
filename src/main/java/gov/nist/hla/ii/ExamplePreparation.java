package gov.nist.hla.ii;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamplePreparation extends InterObjInjectionImpl {

	public ExamplePreparation(InjectionFederate injFed) {
		super(injFed);
	}

	public void prepareInteractions() {
		String interactionName = formatInteractionName("Int1");
		Map<String, String> paramMap = new HashMap<String, String>();
		addInteraction(interactionName, paramMap);
		addInteraction(interactionName, paramMap);
		addInteraction(interactionName, paramMap);
		addInteraction(interactionName, paramMap);
		addInteraction(interactionName, paramMap);
	}

	public void prepareObjects() {
		String objectName = formatObjectName("Obj1");
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("Obj1Attr1", "XXX");
//		addObject(objectName, attrMap);
//		addObject(objectName, attrMap);
//		addObject(objectName, attrMap);
//		addObject(objectName, attrMap);
	}
}
