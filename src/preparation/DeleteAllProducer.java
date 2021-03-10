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

public class DeleteAllProducer extends Producer {
	protected static final Logger logger = LogManager.getLogger();
	
	public DeleteAllProducer()
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		super(new JSAPProvider().getJsap(), "DELETE_ALL", new JSAPProvider().getSecurityManager());

	}
	

	public boolean deleteObservationAlreadyExisting() {
		System.out.println("[DELETE - PRODUCER] - Deleting observation's graph already existing...");

		int retry = 5;

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
		
		if (!ret) logger.error("[DELETE - PRODUCER] DELETE ALL FAILED");

		return ret;
	}
}