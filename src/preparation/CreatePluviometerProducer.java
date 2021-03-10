package preparation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;
import it.unibo.arces.wot.sepa.pattern.Producer;
import utils.JSAPProvider;

public class CreatePluviometerProducer extends Producer {
	protected static final Logger logger = LogManager.getLogger();
	
	public CreatePluviometerProducer()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		super(new JSAPProvider().getJsap(), "ADD_PLUVIOMETER", new JSAPProvider().getSecurityManager());

	}
	

	public boolean createObservation(String location, String observation, String unit, String waterLevel) {
		System.out.println("[CREATE PLUVIOMETER - PRODUCER] - Creating pluviometer linked to features of interest...");

		int retry = 5;

		boolean ret = false;
		while (!ret && retry > 0) {
			try {
				this.setUpdateBindingValue("location", new RDFTermURI(location));
				this.setUpdateBindingValue("observation", new RDFTermURI(observation));
				this.setUpdateBindingValue("unit", new RDFTermURI(unit));
				this.setUpdateBindingValue("waterLevel", new RDFTermLiteral(waterLevel));
				
				ret = update().isUpdateResponse();
			} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
					| SEPABindingsException e) {
				logger.error(e.getMessage());
				ret = false;
			}
			retry--;
		}
		
		if (!ret) logger.error("[CREATE PLUVIOMETER - PRODUCER] CREATION FAILED");

		return ret;
	}
}