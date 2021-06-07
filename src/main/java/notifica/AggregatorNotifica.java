package notifica;

import it.unibo.arces.wot.sepa.pattern.Aggregator;
import utils.JSAPProvider;
import utils.Pluviometer;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;

class AggregatorNotifica extends Aggregator {
	protected static final Logger logger = LogManager.getLogger();
	
	Pluviometer p1 = null;
	ArrayList<Pluviometer> pluviometers = new ArrayList<Pluviometer>();
	int indexToReach, waterLevel;
	private HashMap<String, String> map;
	
	
	public AggregatorNotifica()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException {
		super(new JSAPProvider().getJsap(), "PLUVIOMETERS", "UPDATEWATERLEVEL");
		
		indexToReach=-1;
		waterLevel=0;
		
		map = new HashMap<>();
		map.put("http://swamp-project.org/observation/cbec/013-R24H", "http://wot.arces.unibo.it/waterLevelCorreggio"); //Correggio
		map.put("http://swamp-project.org/observation/cbec/003-R24H", "http://wot.arces.unibo.it/waterLevelSantaMaria"); //Santa Maria
		map.put("http://swamp-project.org/observation/cbec/005-R24H", "http://wot.arces.unibo.it/waterLevelRotte"); //Rotte
	}

	
	@Override
	public void onAddedResults(BindingsResults results) {
		super.onAddedResults(results);

		for (Bindings bindings : results.getBindings()) {
			logger.info("[AGGREGATOR] - Received new value(s): " +bindings.getValue("value") + " for: " +bindings.getValue("observation"));
			
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
			
			logger.info("[SIMULATOR WATER LEVEL] - " + waterLevel + " for: " +bindings.getValue("observation") + " to: "+map.get(bindings.getValue("observation")));
			
			
			int retry = 5;
			
			try {
				this.setUpdateBindingValue("observation", new RDFTermURI(map.get(bindings.getValue("observation"))));
				this.setUpdateBindingValue("waterLevel", new RDFTermLiteral(Integer.toString(waterLevel)));
			} catch (SEPABindingsException e1) {
				logger.error(e1.getMessage());
			}
			
			
			boolean ret = false;
			while (!ret && retry > 0) {
				try {
					ret = update().isUpdateResponse();
					logger.info("[AGGREGATOR] - Updated water level: " + Integer.toString(waterLevel) + " for " +map.get(bindings.getValue("observation")));
				} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
						| SEPABindingsException e) {
					logger.error(e.getMessage());
					ret = false;
				}
				retry--;
			}
			if(!ret) logger.error("[AGGREGATOR] - Failed updated water level(s)");
		}
	}
	
}