package gov.nist.hla.ii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleInject {
	
	private static final Logger log = LogManager
			.getLogger(ExampleInject.class);

	public static void main(String args[]) {
		if (args.length != 1) {
			log.error("command line argument for properties file not specified");
			args = new String[1];
			args[0] = "config.properties";
		}

		try {
			InjectionFederate federate = new InjectionFederate();
			federate.loadConfiguration(args[0]);
			federate.setInterObjectInjection(new ExamplePreparation(federate));
			federate.setInterObjectReception(new ExampleReception());
			federate.run();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
