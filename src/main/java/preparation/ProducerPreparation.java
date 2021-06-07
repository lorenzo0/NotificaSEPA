package preparation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;
import it.unibo.arces.wot.sepa.pattern.JSAP;
import it.unibo.arces.wot.sepa.pattern.Producer;

public class ProducerPreparation extends Producer {
	protected static final Logger logger = LogManager.getLogger();
	
	public ProducerPreparation()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		super(new JSAP("observation_pluviometer.jsap"), "ADD_OBSERVATION");
	}
	

	public boolean createObservation(String location, String observation, String unit, String value,String label) {
		logger.info("[CREATE OBSERVATION - PRODUCER] - Creating observation linked to features of interest...");

		int retry = 5;

		boolean ret = false;
		while (!ret && retry > 0) {
			try {
				this.setUpdateBindingValue("location", new RDFTermURI(location));
				this.setUpdateBindingValue("observation", new RDFTermURI(observation));
				this.setUpdateBindingValue("unit", new RDFTermURI(unit));
				this.setUpdateBindingValue("value", new RDFTermLiteral(value));
				this.setUpdateBindingValue("label", new RDFTermLiteral(value));
				
				ret = update().isUpdateResponse();
			} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
					| SEPABindingsException e) {
				logger.error(e.getMessage());
				ret = false;
			}
			retry--;
		}
		
		if (!ret) logger.error("[CREATE OBSERVATION - PRODUCER] CREATION FAILED");

		return ret;
	}
}