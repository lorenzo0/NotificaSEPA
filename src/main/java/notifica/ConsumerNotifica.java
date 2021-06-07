package notifica;

import it.unibo.arces.wot.sepa.pattern.Consumer;
import utils.JSAPProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;

class ConsumerNotifica extends Consumer {
	protected static final Logger logger = LogManager.getLogger();
	
	public ConsumerNotifica()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException {
		super(new JSAPProvider().getJsap(), "PLUVIOMETERSWITHWATERLEVEL");
	}

	
	@Override
	public void onAddedResults(BindingsResults results) {
		super.onAddedResults(results);

		for (Bindings bindings : results.getBindings()) {
			logger.info("[CONSUMER] - Received new value(s): " +bindings.getValue("waterLevel") + " for: " +bindings.getValue("observation"));
		}
	}
	
}
