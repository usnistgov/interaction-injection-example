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
		addInteration(1D, interactionName, paramMap);
		addInteration(2D, interactionName, paramMap);
		addInteration(3D, interactionName, paramMap);
		addInteration(4D, interactionName, paramMap);
		addInteration(5D, interactionName, paramMap);
	}

	public void prepareObjects() {
		String objectName = formatObjectName("Obj1");
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("Obj1Attr1", "XXX");
		addObject(1D, objectName, attrMap);
		addObject(2D, objectName, attrMap);
		addObject(4D, objectName, attrMap);
		addObject(5D, objectName, attrMap);
	}
}
