package notifica;

import java.util.Iterator;
import java.util.Random; 
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import utils.JSAPProvider;
import preparation.DeleteAllProducer;
import preparation.CreatePlaceProducer;
import preparation.CreateObservationProducer;
import preparation.CreatePluviometerProducer;

public class Main {
	
	/* MANDATORY ENTITY */
	static JSAPProvider jsap;
	static AggregatorNotifica aggregator;
	static ProducerNotifica producer;
	static ConsumerNotifica consumer;
	
	/* PREPARATION OF GRAPH */
	static DeleteAllProducer deleteProducer;
	static CreatePlaceProducer createPlaceProducer;
	static CreateObservationProducer createObsProducer;
	

	public static void main(String[] args) throws SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		
		/* MANDATORY ENTITY */
		jsap = new JSAPProvider();
		aggregator = new AggregatorNotifica();
		producer = new ProducerNotifica();
		consumer = new ConsumerNotifica();
		
		/* PREPARATION OF GRAPH */
		deleteProducer = new DeleteAllProducer();
		createPlaceProducer = new CreatePlaceProducer();
		createObsProducer = new CreateObservationProducer();
		
		/* UTILS */
		Random rand = new Random(); 
		
		preparationDB(deleteProducer, createPlaceProducer, createObsProducer);
		populationSystem(aggregator, consumer);
		System.out.println("\n");
		
		do {
			try {
				producer.sendValue("http://swamp-project.org/observation/cbec/013-R24H", Integer.toString(rand.nextInt(10)));
				producer.sendValue("http://swamp-project.org/observation/cbec/003-R24H", Integer.toString(rand.nextInt(10)));
				producer.sendValue("http://swamp-project.org/observation/cbec/005-R24H", Integer.toString(rand.nextInt(10)));
				System.out.println("\n");
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				return;
			}
			continue;
		} while (true);
		
	}
	
	static void preparationDB(DeleteAllProducer DA, CreatePlaceProducer CPP, CreateObservationProducer COP) {
		DA.deleteObservationAlreadyExisting();
		
		/* Creating features of interest */
		CPP.createPlace("0.1", "0.2", "Correggio", "http://wot.arces.unibo.it/monitor#PluviometroCorreggio");
		CPP.createPlace("0.2", "0.3", "Rotte", "http://wot.arces.unibo.it/monitor#PluviometroRotte");
		CPP.createPlace("0.3", "0.4", "Santa Maria", "http://wot.arces.unibo.it/monitor#PluviometroSantaMaria");
		
		/* Creating pluviometers linked to the features of interests and to its own value*/
		COP.createObservation("http://swamp-project.org/observation/cbec/013-R24H", "comment", "label", "http://wot.arces.unibo.it/monitor#PluviometroCorreggio", "unit:DegreeCelsius");
		COP.createObservation("http://swamp-project.org/observation/cbec/003-R24H", "comment", "label", "http://wot.arces.unibo.it/monitor#PluviometroRotte", "unit:DegreeCelsius");
		COP.createObservation("http://swamp-project.org/observation/cbec/005-R24H", "comment", "label", "http://wot.arces.unibo.it/monitor#PluviometroSantaMaria", "unit:DegreeCelsius");
		
		/* Creating value of pluviometers linked to the features of interests */
		COP.createObservation("http://wot.arces.unibo.it/waterLevelCorreggio", "comment", "label", "http://wot.arces.unibo.it/monitor#PluviometroCorreggio", "unit:DegreeCelsius");
		COP.createObservation("http://wot.arces.unibo.it/waterLevelSantaMaria", "comment", "label", "http://wot.arces.unibo.it/monitor#PluviometroRotte", "unit:DegreeCelsius");
		COP.createObservation("http://wot.arces.unibo.it/waterLevelRotte", "comment", "label", "http://wot.arces.unibo.it/monitor#PluviometroSantaMaria", "unit:DegreeCelsius");
		
		System.out.println("[CHECK] Database ready!");
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
