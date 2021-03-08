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
	static ConsumerNotifica consumer;
	

	public static void main(String[] args) throws SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		
		jsap = new JSAPProvider();
		aggregator = new AggregatorNotifica();
		producer = new ProducerNotifica();
		consumer = new ConsumerNotifica();
		Random rand = new Random(); 
		
		System.out.println("Producer it's here!");
		
		System.out.println("Aggregator is subscribing...");
		aggregator.subscribe();
		System.out.println("Aggregator subscribed succesfully!");
		
		System.out.println("Consumer is subscribing...");
		consumer.subscribe();
		System.out.println("Consumer subscribed succesfully!");
		
		producer.sendValue("http://valorePluv1", Integer.toString(rand.nextInt(100)));
		producer.sendValue("http://valorePluv1", Integer.toString(rand.nextInt(100)));
		
		do {
			try {
				//producer.sendValue("http://valorePluv1", Integer.toString(rand.nextInt(100)));
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				return;
			}
			continue;
		} while (true);
		
	}
	
}
