package ru.sbsoft.system.dao.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Message;
import ru.sbsoft.dao.IMultiOperationManagerDao;

/**
 *
 * @author sychugin
 */
@MessageDriven(name = "JmsReceiver", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = ru.sbsoft.common.jdbc.Const.JMS_QUEUE),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge")})

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@PermitAll
public class JmsReceiver implements MessageListener {

    @EJB
    private IMultiOperationManagerDao operationManagerDao;

    private final Logger logger = Logger.getLogger(JmsReceiver.class.getName());

    // public static List<String> messages = new ArrayList<String>();
    @Override
    public void onMessage(Message rcvMessage) {
        try {
            // messages.add(rcvMessage.getBody(String.class));

            Long operId = rcvMessage.getLongProperty(ru.sbsoft.common.jdbc.Const.OPER_ID);
            String operCode = rcvMessage.getStringProperty(ru.sbsoft.common.jdbc.Const.OPER_CODE);

            operationManagerDao.executeOperation(operId, operCode);

            //rcvMessage.acknowledge();
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
