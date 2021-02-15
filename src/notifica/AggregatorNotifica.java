package notifica;

import it.unibo.arces.wot.sepa.pattern.Aggregator;
import it.unibo.arces.wot.sepa.pattern.JSAP;

import java.util.ArrayList;

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
	
	public AggregatorNotifica(JSAP appProfile, String subscribeID, String updateID, ClientSecurityManager sm)
			throws SEPAProtocolException, SEPASecurityException {
		super(appProfile, subscribeID, updateID, sm);
	}

	
	@Override
	public void onAddedResults(BindingsResults results) {
		super.onAddedResults(results);

		for (Bindings bindings : results.getBindings()) {
			logger.debug("New pluviometer data with uri: " + bindings.getValue("observation"));
			
			String observation = bindings.getValue("observation");
			String value = bindings.getValue("value");
			String timestamp = bindings.getValue("timestamp");	
			int retry = 5;
			
			String newValue = generateNewValue(Double.parseDouble(value));
			try {
				this.setUpdateBindingValue("observation", new RDFTermURI(observation));
				this.setUpdateBindingValue("waterLevel", new RDFTermLiteral(newValue));
			} catch (SEPABindingsException e1) {
				e1.printStackTrace();
			}
			
			
			boolean ret = false;
			while (!ret && retry > 0) {
				try {
					ret = update().isUpdateResponse();
				} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
						| SEPABindingsException e) {
					logger.error(e.getMessage());
					ret = false;
				}
				retry--;
			}
		}
	}
	
	private String generateNewValue(Double value) {
		return "";
	}
	
	
}
