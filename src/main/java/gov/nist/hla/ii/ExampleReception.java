package gov.nist.hla.ii;

import java.util.Arrays;
import java.util.Map;

public class ExampleReception extends InterObjReceptionImpl {

	@Override
	public void receiveInteraction(Double timeStep, String interactionName,
			Map<String, String> parameters) {
		System.out.println(String.format("time step=%d interaction name=%s parameters=%s", timeStep, interactionName, Arrays.toString(parameters.entrySet().toArray())));

	}

	@Override
	public void receiveObject(Double timeStep, String objectClassName, String objectName,
			Map<String, String> attributes) {
		System.out.println(String.format("time step=%d objectClass name=%s object name=%s, attributes=%s", timeStep, objectClassName, objectName, Arrays.toString(attributes.entrySet().toArray())));
	}
}
