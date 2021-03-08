package notifica;

import java.util.Iterator;
import java.util.Random; 
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import utils.JSAPProvider;

public class Main {
	
	static JSAPProvider jsap;
	static AggregatorNotifica aggregator;
	static ProducerNotifica producer;
	

	public static void main(String[] args) throws SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		
		jsap = new JSAPProvider();
		aggregator = new AggregatorNotifica();
		producer = new ProducerNotifica();
		Random rand = new Random(); 
		
		
		System.out.println("Aggregator is subscribing...");
		aggregator.subscribe();
		System.out.println("Aggregator subscribed succesfully!");
		
		//todo - consumer
		
		do {
			try {
				producer.sendValue("http://valorePluv1", Integer.toString(rand.nextInt(100)));
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				return;
			}
			continue;
		} while (true);
		
	}
	
}
