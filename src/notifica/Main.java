package notifica;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Random; 
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import preparation.ProducerPreparation;
import utils.JSAPProvider;

public class Main {
	
	/* MANDATORY ENTITY */
	static JSAPProvider jsap;
	static AggregatorNotifica aggregator;
	static ConsumerNotifica consumer;
	static ProducerPreparation producerPreparation;
	
	protected static final Logger logger = LogManager.getLogger();
	

	public static void main(String[] args) throws SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		
		/* MANDATORY ENTITY */
		jsap = new JSAPProvider();
		aggregator = new AggregatorNotifica();
		consumer = new ConsumerNotifica();
		producerPreparation = new ProducerPreparation();

		//definition of the new observation for water level
		for (int i=0; i < args.length ; i++) {
			if (args[i].equals("-init")) {
				addingObservation(producerPreparation);
				break;
			}
		}
		

		//subscription of aggregator and consumer
		ConsumerNotifica test = null;
		for (int i=0; i < args.length ; i++) {
			if (args[i].equals("-consumer")) {
				test = consumer;	
			}
		}
		populationSystem(aggregator, test);
		
		do {
			;
		} while (true);
		
	}
	
	
	static void populationSystem(AggregatorNotifica A, ConsumerNotifica C) {
		
		try {
			A.subscribe();
			if (C != null) C.subscribe();
		} catch (SEPASecurityException | SEPAPropertiesException | SEPAProtocolException | SEPABindingsException e) {
			e.printStackTrace();
		}
		
		System.out.println("[POPULATION] Aggregator, producer and consumer subscribed succesfully!");		
	}
	
	static void addingObservation(ProducerPreparation PP) {
		PP.createObservation("http://wot.arces.unibo.it/monitor#PluviometroCorreggio>", "http://wot.arces.unibo.it/waterLevelCorreggio", "unit:Number", "-1","013-R24H (Grado di intensità)");
		PP.createObservation("http://wot.arces.unibo.it/monitor#PluviometroRotte", "http://wot.arces.unibo.it/waterLevelRotte", "unit:Number", "-1","005-R24H (Grado di intensità)");
		PP.createObservation("http://wot.arces.unibo.it/monitor#PluviometroSantaMaria", "http://wot.arces.unibo.it/waterLevelSantaMaria", "unit:Number", "-1","003-R24H (Grado di intensità)");
		
		logger.info("[PREPARATION] New  water level's observations created succesfully!");		
	}
	
}