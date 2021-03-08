package notifica;

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

class ProducerNotifica extends Producer {
	protected static final Logger logger = LogManager.getLogger();
	
	public ProducerNotifica()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		super(new JSAPProvider().getJsap(), "UPDATEVALUE", new JSAPProvider().getSecurityManager());

	}
	

	public boolean sendValue(String valorePluvURI, String value) {
		System.out.println("[PRODUCER] - Sending value: " + value + " to:" + valorePluvURI);

		int retry = 5;

		boolean ret = false;
		while (!ret && retry > 0) {
			try {
				this.setUpdateBindingValue("observation", new RDFTermURI(valorePluvURI));
				this.setUpdateBindingValue("value", new RDFTermLiteral(value));

				ret = update().isUpdateResponse();
			} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
					| SEPABindingsException e) {
				logger.error(e.getMessage());
				ret = false;
			}
			retry--;
		}
		
		if (!ret) logger.error("[PRODUCER] UPDATE FAILED");

		return ret;
	}
}