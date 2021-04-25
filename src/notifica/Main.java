package notifica;

import java.util.Iterator;
import java.util.Random; 
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import utils.JSAPProvider;

public class Main {
	
	/* MANDATORY ENTITY */
	static JSAPProvider jsap;
	static AggregatorNotifica aggregator;
	static ConsumerNotifica consumer;
	

	public static void main(String[] args) throws SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		
		/* MANDATORY ENTITY */
		jsap = new JSAPProvider();
		aggregator = new AggregatorNotifica();
		consumer = new ConsumerNotifica();

		populationSystem(aggregator, consumer);
		
		do {
			;
		} while (true);
		
	}
	
	
	static void populationSystem(AggregatorNotifica A, ConsumerNotifica C) {
		
		try {
			A.subscribe();
			C.subscribe();
		} catch (SEPASecurityException | SEPAPropertiesException | SEPAProtocolException | SEPABindingsException e) {
			e.printStackTrace();
		}
		
		System.out.println("[POPULATION] Aggregator, producer and consumer subscribed succesfully!");		
	}
	
}
