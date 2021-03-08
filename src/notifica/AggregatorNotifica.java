package notifica;

import it.unibo.arces.wot.sepa.pattern.Aggregator;
import it.unibo.arces.wot.sepa.pattern.JSAP;
import utils.JSAPProvider;
import utils.Pluviometer;

import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.response.ErrorResponse;
import it.unibo.arces.wot.sepa.commons.security.ClientSecurityManager;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;

class AggregatorNotifica extends Aggregator {
	
	Pluviometer p1 = null;
	ArrayList<Pluviometer> pluviometers = new ArrayList<Pluviometer>();
	int indexToReach, waterLevel;
	private HashMap<String, String> map;
	
	
	public AggregatorNotifica()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException {
		super(new JSAPProvider().getJsap(), "PLUVIOMETERS", "UPDATEWATERLEVEL", new JSAPProvider().getSecurityManager());
		
		indexToReach=-1;
		waterLevel=0;
		
		map = new HashMap<>();
		map.put("http://valorePluv1", "http://pluv1");
		//map.put("http://valorePluv2", "http://pluv2");
		//map.put("http://valorePluv3", "http://pluv3");
	}

	
	@Override
	public void onAddedResults(BindingsResults results) {
		super.onAddedResults(results);

		for (Bindings bindings : results.getBindings()) {
			System.out.println("[AGGREGATOR] - Received new value(s): " +bindings.getValue("value") + " for: " +bindings.getValue("observation"));
		
			/*logger.debug("New pluviometer data with uri: " + bindings.getValue("observation"));
			System.out.println("Observation: " +bindings.getValue("observation"));
			System.out.println("Value: " +bindings.getValue("value"));
			System.out.println("Timestamp: " +bindings.getValue("timestamp"));*/
			
			for(int i=0; i<pluviometers.size(); i++) {
				if(pluviometers.get(i).getObservation().equals(bindings.getValue("observation")))
					indexToReach = i;
			}
			
			if(indexToReach!=-1) {
				pluviometers.get(indexToReach).updateValues(Double.parseDouble(bindings.getValue("value")),
															bindings.getValue("timestamp"));
				waterLevel = pluviometers.get(indexToReach).getNewWaterValue();
			}else {
				p1 = new Pluviometer(bindings.getValue("observation"),
						 Double.parseDouble(bindings.getValue("value")),
						 bindings.getValue("timestamp"));
				pluviometers.add(p1);
				waterLevel = pluviometers.get(pluviometers.indexOf(p1)).getNewWaterValue();
			}
			
			
			int retry = 5;
			
			try {
				this.setUpdateBindingValue("observation", new RDFTermURI(map.get(bindings.getValue("observation"))));
				this.setUpdateBindingValue("waterLevel", new RDFTermLiteral(Integer.toString(waterLevel)));
			} catch (SEPABindingsException e1) {
				e1.printStackTrace();
			}
			
			
			boolean ret = false;
			while (!ret && retry > 0) {
				try {
					ret = update().isUpdateResponse();
					System.out.println("[AGGREGATOR] - Updated water level: " + Integer.toString(waterLevel) + " for " +map.get(bindings.getValue("observation")));
				} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
						| SEPABindingsException e) {
					logger.error(e.getMessage());
					ret = false;
				}
				retry--;
			}
			if(!ret) System.out.println("[AGGREGATOR] - Failed updated water level(s)");
		}
	}
	
}
