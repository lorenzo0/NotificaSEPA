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

public class CreatePlaceProducer extends Producer {
	protected static final Logger logger = LogManager.getLogger();
	
	public CreatePlaceProducer()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		super(new JSAPProvider().getJsap(), "ADD_PLACE", new JSAPProvider().getSecurityManager());

	}
	

	public boolean createPlace(String lat, String lon, String name, String place) {
		System.out.println("[CREATE PLACE - PRODUCER] - Creating features of interest...");

		int retry = 5;

		boolean ret = false;
		while (!ret && retry > 0) {
			try {
				this.setUpdateBindingValue("lat", new RDFTermLiteral(lat));
				this.setUpdateBindingValue("lon", new RDFTermLiteral(lon));
				this.setUpdateBindingValue("name", new RDFTermLiteral(name));
				this.setUpdateBindingValue("place", new RDFTermURI(place));
				
				ret = update().isUpdateResponse();
			} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
					| SEPABindingsException e) {
				logger.error(e.getMessage());
				ret = false;
			}
			retry--;
		}
		
		if (!ret) logger.error("[CREATE PLACE - PRODUCER] CREATION FAILED");

		return ret;
	}
}