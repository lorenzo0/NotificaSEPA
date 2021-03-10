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
	static CreatePluviometerProducer createPluvProducer;
	

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
		createPluvProducer = new CreatePluviometerProducer();
		
		/* UTILS */
		Random rand = new Random(); 
		
		preparationDB(deleteProducer, createPlaceProducer, createObsProducer, createPluvProducer);
		populationSystem(aggregator, consumer);
		
		do {
			try {
				producer.sendValue("http://valorePluv1", Integer.toString(rand.nextInt(40)));
				producer.sendValue("http://valorePluv2", Integer.toString(rand.nextInt(40)));
				producer.sendValue("http://valorePluv3", Integer.toString(rand.nextInt(40)));
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				return;
			}
			continue;
		} while (true);
		
	}
	
	static void preparationDB(DeleteAllProducer DA, CreatePlaceProducer CPP, CreateObservationProducer COP, CreatePluviometerProducer CPLP) {
		DA.deleteObservationAlreadyExisting();
		
		/* Creating features of interest */
		CPP.createPlace("0.1", "0.2", "place1", "http://place1");
		CPP.createPlace("0.2", "0.3", "place2", "http://place2");
		CPP.createPlace("0.3", "0.4", "place3", "http://place3");
		
		/* Creating value of pluviometers linked to the features of interests */
		COP.createObservation("http://place1", "http://valorePluv1", "unit:DegreeCelsius", "-1");
		COP.createObservation("http://place2", "http://valorePluv2", "unit:DegreeCelsius", "-1");
		COP.createObservation("http://place3", "http://valorePluv3", "unit:DegreeCelsius", "-1");
		
		/* Creating pluviometers linked to the features of interests and to its own value*/
		CPLP.createObservation("http://place1", "http://pluv1", "unit:DegreeCelsius", "-2");
		CPLP.createObservation("http://place2", "http://pluv2", "unit:DegreeCelsius", "-2");
		CPLP.createObservation("http://place3", "http://pluv3", "unit:DegreeCelsius", "-2");
		
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
