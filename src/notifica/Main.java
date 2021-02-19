package notifica;

import java.util.Iterator;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;

public class Main {

	public static void main(String[] args) throws SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		
		JSAPProvider jsap = new JSAPProvider();
		AggregatorNotifica aggregator = new AggregatorNotifica(jsap.getJsap(), "PLUVIOMETERS", "UPDATEWATERLEVEL", jsap.getSecurityManager());
		
		aggregator.subscribe();
		System.out.println("Aggregator is waiting....");
		while(true) {
		}
	}
}
